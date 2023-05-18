package com.example.graphqlexample.config;

import io.r2dbc.spi.ConnectionFactory;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.r2dbc.core.DefaultReactiveDataAccessStrategy;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.dialect.PostgresDialect;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import org.springframework.r2dbc.core.DatabaseClient;


@Configuration
@EnableR2dbcRepositories(
        entityOperationsRef = "customersEntityTemplate",
        basePackages = {"com.example.graphqlexample.domain.customer"}
)
@Slf4j
public class CustomerConfig {
    private final ConnectionFactory connectionFactory;

    public CustomerConfig(@Qualifier("customersConnectionFactory") ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Bean
    @Qualifier(value = "consumerDatabaseClient")
    public DatabaseClient customerDatabaseClient() {
        return DatabaseClient.builder()
                .connectionFactory(connectionFactory)
                .bindMarkers(PostgresDialect.INSTANCE.getBindMarkersFactory())
                .build();
    }

    @Bean
    public R2dbcEntityOperations customersEntityTemplate(
            @Qualifier(value = "consumerDatabaseClient") DatabaseClient client
    ) {
        DefaultReactiveDataAccessStrategy strategy = new DefaultReactiveDataAccessStrategy(PostgresDialect.INSTANCE);
        return new R2dbcEntityTemplate(client, strategy);
    }

    @PostConstruct
    public void initialize() {
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        databasePopulator.addScripts(
                new ClassPathResource("scripts/customers/schema.sql"),
                new ClassPathResource("scripts/customers/data.sql")
        );
        databasePopulator.populate(connectionFactory)
                .doOnError(throwable -> log.error("init failed {}", throwable.getMessage()))
                .subscribe();
    }

}

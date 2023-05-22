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
        entityOperationsRef = "transactionsEntityTemplate",
        basePackages = {"com.example.graphqlexample.domain.transaction"}
)
@Slf4j
public class TransactionConfig {
    private final ConnectionFactory connectionFactory;

    public TransactionConfig(@Qualifier("transactionsConnectionFactory") ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Bean
    @Qualifier(value = "transactionDatabaseClient")
    public DatabaseClient transactionDatabaseClient(@Qualifier("transactionsConnectionFactory") ConnectionFactory connectionFactory) {
        return DatabaseClient.builder()
                .connectionFactory(connectionFactory)
                .bindMarkers(PostgresDialect.INSTANCE.getBindMarkersFactory())
                .build();
    }

    @Bean(name = "transactionsEntityTemplate")
    public R2dbcEntityOperations transactionsEntityTemplate(
            @Qualifier(value = "transactionDatabaseClient") DatabaseClient client
    ) {
        DefaultReactiveDataAccessStrategy strategy = new DefaultReactiveDataAccessStrategy(PostgresDialect.INSTANCE);
        return new R2dbcEntityTemplate(client, strategy);
    }

    @PostConstruct
    public void initialize() {
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        String name = connectionFactory.getMetadata().getName();
        if (name.equals("H2")) {
            databasePopulator.addScripts(
                    new ClassPathResource("scripts/transactions/init.sql")
            );
        }
        databasePopulator.addScripts(
                new ClassPathResource("scripts/transactions/schema.sql"),
                new ClassPathResource("scripts/transactions/data.sql")
        );
        databasePopulator.populate(connectionFactory)
                .doOnError(throwable -> log.error("init failed {}", throwable.getMessage()))
                .subscribe();
    }

}

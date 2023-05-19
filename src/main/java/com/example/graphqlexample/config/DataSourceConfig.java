package com.example.graphqlexample.config;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig {
    @Bean
    @Qualifier(value = "customersConnectionFactory")
    public ConnectionFactory customersConnectionFactory(@Value("${spring.datasource.customer.url}") String conn) {
        return ConnectionFactories.get(conn);
    }

    @Bean
    @Qualifier(value = "transactionsConnectionFactory")
    public ConnectionFactory transactionsConnectionFactory(@Value("${spring.datasource.transaction.url}") String conn) {
        return ConnectionFactories.get(conn);
    }
}

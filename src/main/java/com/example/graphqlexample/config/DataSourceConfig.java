package com.example.graphqlexample.config;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig {
    @Bean
    @Qualifier(value = "customersConnectionFactory")
    public ConnectionFactory customersConnectionFactory() {
        return ConnectionFactories.get("r2dbc:postgres://postgres:postgres@localhost/customers");
    }
}

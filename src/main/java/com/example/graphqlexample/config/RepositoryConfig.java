package com.example.graphqlexample.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.convert.MappingR2dbcConverter;
import org.springframework.data.r2dbc.mapping.R2dbcMappingContext;

@Configuration
public class RepositoryConfig {
    @Bean
    public R2dbcMappingContext r2dbcMappingContext() {
        return new R2dbcMappingContext();
    }

    @Bean
    public MappingR2dbcConverter mappingR2dbcConverter(R2dbcMappingContext mappingContext) {
        return new MappingR2dbcConverter(mappingContext);
    }
}

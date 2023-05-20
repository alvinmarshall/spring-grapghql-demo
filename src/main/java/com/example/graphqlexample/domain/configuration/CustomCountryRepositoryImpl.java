package com.example.graphqlexample.domain.configuration;

import com.example.graphqlexample.mapper.configuration.CountryRowMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class CustomCountryRepositoryImpl implements CustomCountryRepository {
    private final DatabaseClient client;
    private final CountryRowMapper countryRowMapper;

    public CustomCountryRepositoryImpl(
            @Qualifier("configurationDatabaseClient") DatabaseClient client,
            CountryRowMapper countryRowMapper
    ) {
        this.client = client;
        this.countryRowMapper = countryRowMapper;
    }

    @Override
    public Mono<Country> findByIdWithStates(String iso3Code) {
        return client.sql(CountryQuery.SELECT_COUNTRY_WITH_STATES)
                .bind("iso3Code", iso3Code)
                .map(countryRowMapper)
                .one();
    }
}

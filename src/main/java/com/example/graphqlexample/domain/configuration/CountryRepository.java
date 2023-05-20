package com.example.graphqlexample.domain.configuration;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface CountryRepository extends R2dbcRepository<Country, String>, CustomCountryRepository {
    Mono<Country> findByCountryId(String code);
}

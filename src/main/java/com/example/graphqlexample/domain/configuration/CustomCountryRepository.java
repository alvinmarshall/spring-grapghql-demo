package com.example.graphqlexample.domain.configuration;

import reactor.core.publisher.Mono;

public interface CustomCountryRepository {
    Mono<Country> findByIdWithStates(String iso3Code);
}

package com.example.graphqlexample.domain.configuration;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface StateRepository extends R2dbcRepository<State, String> {
    Flux<State> findByCountry_CountryId(String countryId);
}

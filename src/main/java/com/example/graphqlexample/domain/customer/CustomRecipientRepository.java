package com.example.graphqlexample.domain.customer;

import reactor.core.publisher.Flux;

import java.util.Map;

public interface CustomRecipientRepository {
    Flux<Map<Object, Object>> countRecipientsByCountry(String customerId);
}

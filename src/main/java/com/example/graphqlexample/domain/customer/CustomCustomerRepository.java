package com.example.graphqlexample.domain.customer;

import reactor.core.publisher.Mono;

public interface CustomCustomerRepository {
    Mono<Customer> findCustomerById(String id);
}

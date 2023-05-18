package com.example.graphqlexample.data.repository.customer;

import com.example.graphqlexample.domain.customer.Customer;
import reactor.core.publisher.Mono;

public interface CustomCustomerRepository {
    Mono<Customer> findCustomerById(String id);
}

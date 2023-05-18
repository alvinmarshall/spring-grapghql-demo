package com.example.graphqlexample.domain.customer;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface CustomerRepository extends R2dbcRepository<Customer, String>, CustomCustomerRepository {
}

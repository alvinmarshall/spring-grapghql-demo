package com.example.graphqlexample.data.repository.customer;

import com.example.graphqlexample.domain.customer.Customer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CustomerRepository extends ReactiveCrudRepository<Customer, String>, CustomCustomerRepository {
}

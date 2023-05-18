package com.example.graphqlexample.web;


import com.example.graphqlexample.data.repository.customer.CustomerRepository;
import com.example.graphqlexample.domain.customer.Customer;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class CustomersController {
    private final CustomerRepository customerRepository;

    public CustomersController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @QueryMapping
    public Flux<Customer> customers() {
        return customerRepository.findAll();
    }

    @QueryMapping
    public Mono<Customer> customer(@Argument String id) {
        return customerRepository.findCustomerById(id);
    }
}

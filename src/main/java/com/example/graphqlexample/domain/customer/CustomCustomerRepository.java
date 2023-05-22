package com.example.graphqlexample.domain.customer;

import com.example.graphqlexample.dto.CountItem;
import com.example.graphqlexample.dto.customer.CountTier;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomCustomerRepository {
    Mono<Customer> findCustomerById(String id);

    Flux<Customer> findAllCustomers();

    Flux<CountItem> countCustomersNationalityByKycStatus(KycStatus status);

    Flux<CountItem> countCustomersStateByKycStatus(KycStatus status);

    Flux<CountTier> countCustomersTier();
}

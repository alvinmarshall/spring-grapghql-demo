package com.example.graphqlexample.domain.customer;

import com.example.graphqlexample.dto.customer.CountRecipientCountry;
import reactor.core.publisher.Flux;

public interface CustomRecipientRepository {
    Flux<CountRecipientCountry> countRecipientsByCountry(String customerId);
}

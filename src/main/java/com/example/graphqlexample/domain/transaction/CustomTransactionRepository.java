package com.example.graphqlexample.domain.transaction;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomTransactionRepository {
    Flux<Transaction> findTransactionByCustomer(String customerId);

    Mono<Long> countByCustomer(String customerId);

    Mono<Long> countStatusByCustomer(TransactionStatus status, String customerId);
}

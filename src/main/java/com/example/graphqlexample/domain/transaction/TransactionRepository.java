package com.example.graphqlexample.domain.transaction;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface TransactionRepository extends R2dbcRepository<Transaction, String>, CustomTransactionRepository {
    Mono<Long> countTransactionsByType(TransactionType type);
}

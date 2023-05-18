package com.example.graphqlexample.domain.transaction;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface TransactionRepository extends R2dbcRepository<Transaction, String>, CustomTransactionRepository {
}

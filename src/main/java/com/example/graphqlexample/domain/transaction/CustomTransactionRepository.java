package com.example.graphqlexample.domain.transaction;

import com.example.graphqlexample.dto.transaction.CountTransactionByRecipient;
import com.example.graphqlexample.dto.transaction.SumTransactionByRecipient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomTransactionRepository {
    Flux<Transaction> findTransactionByCustomer(String customerId);

    Mono<Long> countByCustomer(String customerId);

    Mono<Long> countStatusByCustomer(TransactionStatus status, String customerId);

    Flux<CountTransactionByRecipient> countAndGroupByRecipients();

    Flux<CountTransactionByRecipient> countTypeAndGroupByRecipients(TransactionType type);

    Flux<SumTransactionByRecipient> sumFromAmountAndGroupByRecipients();
}

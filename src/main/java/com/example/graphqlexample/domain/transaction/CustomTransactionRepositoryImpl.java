package com.example.graphqlexample.domain.transaction;

import com.example.graphqlexample.mapper.transaction.TransactionRowMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class CustomTransactionRepositoryImpl implements CustomTransactionRepository {
    private final DatabaseClient client;
    private final TransactionRowMapper transactionRowMapper;
    private static final String CUSTOMER_ID = "customerId";

    public CustomTransactionRepositoryImpl(
            @Qualifier("transactionDatabaseClient") DatabaseClient client,
            TransactionRowMapper transactionRowMapper
    ) {
        this.client = client;
        this.transactionRowMapper = transactionRowMapper;
    }

    @Override
    public Flux<Transaction> findTransactionByCustomer(String customerId) {
        return client.sql("%s WHERE t.user_id = :customerId".formatted(TransactionQuery.SELECT_QUERY))
                .bind(CUSTOMER_ID, customerId)
                .map(transactionRowMapper)
                .all();
    }

    @Override
    public Mono<Long> countByCustomer(String customerId) {
        return client.sql("%s WHERE user_id = :customerId".formatted(TransactionQuery.COUNT_QUERY))
                .bind(CUSTOMER_ID, customerId)
                .map((row, rowMetadata) -> row.get("count", Long.class))
                .one();
    }

    @Override
    public Mono<Long> countStatusByCustomer(TransactionStatus status, String customerId) {
        return client.sql("%s WHERE status = :status AND user_id = :customerId".formatted(TransactionQuery.COUNT_QUERY))
                .bind(CUSTOMER_ID, customerId)
                .bind("status", status.name())
                .map((row, rowMetadata) -> row.get("count", Long.class))
                .one();
    }
}

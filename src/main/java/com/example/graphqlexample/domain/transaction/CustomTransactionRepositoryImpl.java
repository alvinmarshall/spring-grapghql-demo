package com.example.graphqlexample.domain.transaction;

import com.example.graphqlexample.dto.transaction.CountTransactionByRecipient;
import com.example.graphqlexample.dto.transaction.SumTransactionByRecipient;
import com.example.graphqlexample.mapper.transaction.TransactionRowMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Repository
public class CustomTransactionRepositoryImpl implements CustomTransactionRepository {
    private final DatabaseClient client;
    private final TransactionRowMapper transactionRowMapper;
    private static final String CUSTOMER_ID = "customerId";
    private static final String COUNT = "count";
    private static final String RECIPIENT_ID = "recipient_id";

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
                .map((row, rowMetadata) -> row.get(COUNT, Long.class))
                .one();
    }

    @Override
    public Mono<Long> countStatusByCustomer(TransactionStatus status, String customerId) {
        return client.sql("%s WHERE status = :status AND user_id = :customerId".formatted(TransactionQuery.COUNT_QUERY))
                .bind(CUSTOMER_ID, customerId)
                .bind("status", status.name())
                .map((row, rowMetadata) -> row.get(COUNT, Long.class))
                .one();
    }

    @Override
    public Flux<CountTransactionByRecipient> countAndGroupByRecipients() {
        return client.sql(TransactionQuery.COUNT_AND_GROUP_BY_RECIPIENTS)
                .map((row, rowMetadata) -> {
                    Long count = row.get(COUNT, Long.class);
                    String recipientId = row.get(RECIPIENT_ID, String.class);
                    return CountTransactionByRecipient.builder().count(count).recipientId(recipientId).build();
                })
                .all();
    }

    @Override
    public Flux<CountTransactionByRecipient> countTypeAndGroupByRecipients(TransactionType type) {
        return client.sql(TransactionQuery.COUNT_TYPE_AND_GROUP_BY_RECIPIENTS)
                .bind("type", type.name())
                .map((row, rowMetadata) -> {
                    Long count = row.get(COUNT, Long.class);
                    String recipientId = row.get(RECIPIENT_ID, String.class);
                    return CountTransactionByRecipient.builder().count(count).recipientId(recipientId).build();
                })
                .all();
    }

    @Override
    public Flux<SumTransactionByRecipient> sumFromAmountAndGroupByRecipients() {
        return client.sql(TransactionQuery.SUM_FROM_AMOUNT_AND_GROUP_BY_RECIPIENTS)
                .map((row, rowMetadata) -> {
                    BigDecimal amount = row.get("amount", BigDecimal.class);
                    String recipientId = row.get(RECIPIENT_ID, String.class);
                    String currency = row.get("from_currency", String.class);
                    return SumTransactionByRecipient.builder()
                            .currency(currency).amount(amount).recipientId(recipientId)
                            .build();
                })
                .all();
    }
}

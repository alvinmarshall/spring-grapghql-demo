package com.example.graphqlexample.domain.transaction;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.function.BiFunction;

@Repository
public class CustomTransactionRepositoryImpl implements CustomTransactionRepository {
    private final DatabaseClient client;

    public CustomTransactionRepositoryImpl(@Qualifier("transactionDatabaseClient") DatabaseClient client) {
        this.client = client;
    }

    public static final BiFunction<Row, RowMetadata, Transaction> MAPPING_FUNCTION =
            (row, rowMetaData) -> Transaction.builder()
                    .id(row.get("t_id", String.class))
                    .bonusAmount(row.get("t_bonus_amount", BigDecimal.class))
                    .exchangeRate(row.get("t_exchange_rate", BigDecimal.class))
                    .feeAmount(row.get("t_fee_amount", BigDecimal.class))
                    .fromAmount(row.get("t_from_amount", BigDecimal.class))
                    .toAmount(row.get("t_to_amount", BigDecimal.class))
                    .toCurrency(row.get("t_to_currency", String.class))
                    .status(row.get("t_status", String.class))
                    .transactionId(row.get("t_transaction_id", String.class))
                    .couponCode(row.get("t_coupon_code", String.class))
                    .reason(row.get("t_reason", String.class))
                    .reason(row.get("t_reference_number", String.class))
                    .userId(row.get("t_user_id", String.class))
                    .senderId(row.get("t_sender_id", String.class))
                    .build();

    @Override
    public Mono<Transaction> findTransactionById(String id) {
        return client.sql("%s WHERE t.id = :id LIMIT 1".formatted(TransactionQuery.SELECT_QUERY))
                .bind("id", id)
                .map(MAPPING_FUNCTION)
                .one();
    }

    @Override
    public Flux<Transaction> findTransactionByCustomer(String customerId) {
        return client.sql("%s WHERE t.user_id = :customerId".formatted(TransactionQuery.SELECT_QUERY))
                .bind("customerId", customerId)
                .map(MAPPING_FUNCTION)
                .all();
    }

    @Override
    public Mono<Long> countByCustomer(String customerId) {
        return client.sql("%s WHERE user_id = :customerId".formatted(TransactionQuery.COUNT_QUERY))
                .bind("customerId", customerId)
                .map((row, rowMetadata) -> row.get("count", Long.class))
                .one();
    }

    @Override
    public Mono<Long> countStatusByCustomer(TransactionStatus status, String customerId) {
        return client.sql("%s WHERE status = :status AND user_id = :customerId".formatted(TransactionQuery.COUNT_QUERY))
                .bind("customerId", customerId)
                .bind("status", status.name())
                .map((row, rowMetadata) -> row.get("count", Long.class))
                .one();
    }
}

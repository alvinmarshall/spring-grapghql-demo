package com.example.graphqlexample.mapper.transaction;

import com.example.graphqlexample.domain.customer.Recipient;
import com.example.graphqlexample.domain.transaction.Transaction;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.function.BiFunction;

@Component
public class TransactionRowMapper implements BiFunction<Row, RowMetadata, Transaction> {
    @Override
    public Transaction apply(Row row, RowMetadata rowMetadata) {
        return Transaction.builder()
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
                .recipient(Recipient.builder().id(row.get("t_recipient_id", String.class)).build())
                .build();
    }
}

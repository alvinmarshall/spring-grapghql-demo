package com.example.graphqlexample.domain.transaction;

import com.example.graphqlexample.domain.customer.Recipient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.math.BigDecimal;

@Table(name = "transactions")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction implements Serializable {
    @Id
    private String id;
    @Column(value = "bonus_amount")
    private BigDecimal bonusAmount;
    @Column(value = "fee_amount")
    private BigDecimal feeAmount;
    @Column(value = "from_amount")
    private BigDecimal fromAmount;
    @Column(value = "exchange_rate")
    private BigDecimal exchangeRate;
    @Column(value = "to_amount")
    private BigDecimal toAmount;
    private String status;
    @Column(value = "user_id")
    private String userId;
    @Column(value = "sender_d")
    private String senderId;
    @Column(value = "reference_number")
    private String referenceNumber;
    @Column(value = "fund_source_type")
    private String fundSourceType;
    @Column(value = "transaction_id")
    private String transactionId;
    @Column(value = "from_currency")
    private String fromCurrency;
    @Column(value = "to_currency")
    private String toCurrency;
    private String reason;
    @Column(value = "coupon_code")
    private String couponCode;
    private Recipient recipient;
}

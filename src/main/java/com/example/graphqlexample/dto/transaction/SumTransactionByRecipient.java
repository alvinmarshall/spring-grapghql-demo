package com.example.graphqlexample.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SumTransactionByRecipient {
    private BigDecimal amount;
    private String recipientId;
    private String currency;
}

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
public class SumAmountRecipientState {
    private BigDecimal amount;
    private String state;
    private String country;
    private String currency;
}

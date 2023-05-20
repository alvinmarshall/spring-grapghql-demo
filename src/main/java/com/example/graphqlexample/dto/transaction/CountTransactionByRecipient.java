package com.example.graphqlexample.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CountTransactionByRecipient {
    private Long count;
    private String recipientId;
}

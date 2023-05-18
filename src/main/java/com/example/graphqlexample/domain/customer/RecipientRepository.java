package com.example.graphqlexample.domain.customer;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface RecipientRepository extends R2dbcRepository<Recipient, String>, CustomRecipientRepository {
}

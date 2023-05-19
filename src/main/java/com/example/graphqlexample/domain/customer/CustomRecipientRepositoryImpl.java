package com.example.graphqlexample.domain.customer;

import com.example.graphqlexample.dto.customer.CountRecipientCountry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public class CustomRecipientRepositoryImpl implements CustomRecipientRepository {
    private final DatabaseClient client;

    public CustomRecipientRepositoryImpl(@Qualifier("customerDatabaseClient") DatabaseClient client) {
        this.client = client;
    }

    @Override
    public Flux<CountRecipientCountry> countRecipientsByCountry(String customerId) {
        return client.sql(RecipientQuery.GROUP_CUSTOMER_RECIPIENTS_BY_COUNTRY)
                .bind("customerId", customerId)
                .map((row, rowMetadata) -> CountRecipientCountry.builder()
                        .count(row.get("count", Long.class))
                        .country(row.get("country", String.class))
                        .build()
                )
                .all();
    }
}

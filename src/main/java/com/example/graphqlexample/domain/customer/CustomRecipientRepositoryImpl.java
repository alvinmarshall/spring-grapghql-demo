package com.example.graphqlexample.domain.customer;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

@Repository
public class CustomRecipientRepositoryImpl implements CustomRecipientRepository {
    private static final BiFunction<Row, RowMetadata, Recipient> MAPPING_FUNCTION =
            (row, rowMetadata) -> Recipient.builder()
                    .id(row.get("id", String.class))
                    .firstName(row.get("first_name", String.class))
                    .lastName(row.get("last_name", String.class))
                    .middleName(row.get("middle_name", String.class))
                    .email(row.get("email", String.class))
                    .mobilePhone(row.get("mobile_phone", String.class))
                    .addressLine1(row.get("address_line1", String.class))
                    .province(row.get("province", String.class))
                    .zipcode(row.get("zip_code", String.class))
                    .country(row.get("country", String.class))
                    .customer(Customer.builder()
                            .id(row.get("user_id", String.class))
                            .build())
                    .build();
    private final DatabaseClient client;

    public CustomRecipientRepositoryImpl(@Qualifier("customerDatabaseClient") DatabaseClient client) {
        this.client = client;
    }

    @Override
    public Flux<Map<Object, Object>> countRecipientsByCountry(String customerId) {
        return client.sql(RecipientQuery.GROUP_CUSTOMER_RECIPIENTS_BY_COUNTRY)
                .bind("customerId", customerId)
                .map((row, rowMetadata) -> {
                    Long count = Optional.ofNullable(row.get("count", Long.class)).orElse(0L);
                    String country = Optional.ofNullable(row.get("country", String.class)).orElse("");
                    return Map.<Object, Object>of("count", count, "country", country);
                })
                .all();
    }
}

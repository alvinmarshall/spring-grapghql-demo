package com.example.graphqlexample.mapper.customer;

import com.example.graphqlexample.domain.customer.Recipient;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import org.springframework.stereotype.Component;

import java.util.function.BiFunction;

@Component
public class RecipientRowMapper implements BiFunction<Row, RowMetadata, Recipient> {
    @Override
    public Recipient apply(Row row, RowMetadata rowMetadata) {
        return Recipient.builder()
                .id(row.get("r_id", String.class))
                .firstName(row.get("r_first_name", String.class))
                .lastName(row.get("r_last_name", String.class))
                .middleName(row.get("r_middle_name", String.class))
                .email(row.get("r_email", String.class))
                .mobilePhone(row.get("r_mobile_phone", String.class))
                .addressLine1(row.get("r_address_line1", String.class))
                .city(row.get("r_city", String.class))
                .country(row.get("r_country", String.class))
                .province(row.get("r_province", String.class))
                .zipcode(row.get("r_zip_code", String.class))
                .build();
    }
}

package com.example.graphqlexample.mapper.configuration;

import com.example.graphqlexample.domain.configuration.Country;
import com.example.graphqlexample.domain.configuration.State;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import org.springframework.stereotype.Component;

import java.util.function.BiFunction;

@Component
public class StateRowMapper implements BiFunction<Row, RowMetadata, State> {
    @Override
    public State apply(Row row, RowMetadata rowMetadata) {
        return State.builder()
                .id(row.get("s_id", String.class))
                .name(row.get("s_name", String.class))
                .code(row.get("s_code", String.class))
                .country(getCountry(row))
                .build();
    }

    private Country getCountry(Row row) {
        return Country.builder()
                .countryIso3code(row.get("c_country_iso3_code", String.class))
                .countryIso2code(row.get("c_country_iso2_code", String.class))
                .name(row.get("c_name", String.class))
                .phoneCode(row.get("c_phone_code", String.class))
                .countryId(row.get("c_country_id", String.class))
                .build();
    }
}

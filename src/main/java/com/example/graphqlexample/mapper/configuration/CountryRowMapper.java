package com.example.graphqlexample.mapper.configuration;

import com.example.graphqlexample.domain.configuration.Country;
import com.example.graphqlexample.domain.configuration.State;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import org.springframework.data.r2dbc.convert.MappingR2dbcConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class CountryRowMapper implements BiFunction<Row, RowMetadata, Country> {
    private final MappingR2dbcConverter converter;

    public CountryRowMapper(MappingR2dbcConverter converter) {
        this.converter = converter;
    }

    @Override
    public Country apply(Row row, RowMetadata rowMetadata) {
        return Country.builder()
                .countryIso3code(row.get("c_country_iso3_code", String.class))
                .countryIso2code(row.get("c_country_iso2_code", String.class))
                .name(row.get("c_name", String.class))
                .phoneCode(row.get("c_phone_code", String.class))
                .countryId(row.get("c_country_id", String.class))
                .states(getStates(row))
                .build();
    }

    private Set<State> getStates(Row row) {
        String[] sIds = Optional.ofNullable(converter.getConversionService()
                        .convert(row.get("s_id", Object.class), String[].class))
                .orElse(new String[]{});

        String[] sNames = Optional.ofNullable(converter.getConversionService()
                        .convert(row.get("s_name", Object.class), String[].class))
                .orElse(new String[]{});

        String[] sCodes = Optional.ofNullable(converter.getConversionService()
                        .convert(row.get("s_code", Object.class), String[].class))
                .orElse(new String[]{});

        if (ObjectUtils.isEmpty(sIds)) return Collections.emptySet();

        return IntStream.range(0, sIds.length)
                .mapToObj(index -> State.builder()
                        .id(sIds[index])
                        .name(sNames[index])
                        .code(sCodes[index])
                        .build())
                .collect(Collectors.toSet());
    }


}

package com.example.graphqlexample.mapper.customer;

import com.example.graphqlexample.domain.customer.Customer;
import com.example.graphqlexample.domain.customer.Recipient;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import org.springframework.data.r2dbc.convert.MappingR2dbcConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class CustomerRowMapper implements BiFunction<Row, RowMetadata, Customer> {
    private final MappingR2dbcConverter converter;

    public CustomerRowMapper(MappingR2dbcConverter converter) {
        this.converter = converter;
    }


    @Override
    public Customer apply(Row row, RowMetadata rowMetadata) {
        return Customer.builder()
                .id(row.get("u_id", String.class))
                .firstName(row.get("u_first_name", String.class))
                .lastName(row.get("u_last_name", String.class))
                .middleName(row.get("u_middle_name", String.class))
                .email(row.get("u_email", String.class))
                .mobilePhone(row.get("u_mobile_phone", String.class))
                .mobileCountryCode(row.get("u_mobile_country_code", String.class))
                .gender(row.get("u_gender", String.class))
                .city(row.get("u_city", String.class))
                .state(row.get("u_state", String.class))
                .addressLine1(row.get("u_address_line1", String.class))
                .zipcode(row.get("u_zipcode", String.class))
                .nationality(row.get("u_nationality", String.class))
                .externalProviderId(row.get("u_external_provider_id", String.class))
                .tier(row.get("u_tier", String.class))
                .dob(row.get("u_dob", LocalDate.class))
                .kycStatus(row.get("u_kyc_status", String.class))
                .recipients(getRecipients(row))
                .build();
    }

    private Set<Recipient> getRecipients(Row row) {
        String[] rIds = Optional.ofNullable(converter.getConversionService()
                        .convert(row.get("r_id", Object.class), String[].class))
                .orElse(new String[]{});

        String[] rFirstNames = Optional.ofNullable(
                        converter.getConversionService().convert(row.get("r_first_name", Object.class), String[].class))
                .orElse(new String[]{});

        String[] rLastNames = Optional.ofNullable(
                        converter.getConversionService().convert(row.get("r_last_name", Object.class), String[].class))
                .orElse(new String[]{});

        String[] rMiddleNames = Optional.ofNullable(
                        converter.getConversionService().convert(row.get("r_middle_name", Object.class), String[].class))
                .orElse(new String[]{});

        String[] rEmails = Optional.ofNullable(
                        converter.getConversionService().convert(row.get("r_email", Object.class), String[].class))
                .orElse(new String[]{});

        String[] rMobilePhones = Optional.ofNullable(
                        converter.getConversionService().convert(row.get("r_mobile_phone", Object.class), String[].class))
                .orElse(new String[]{});

        String[] rCities = Optional.ofNullable(
                        converter.getConversionService().convert(row.get("r_city", Object.class), String[].class))
                .orElse(new String[]{});

        String[] rProvinces = Optional.ofNullable(
                        converter.getConversionService().convert(row.get("r_province", Object.class), String[].class))
                .orElse(new String[]{});

        String[] rZipCodes = Optional.ofNullable(
                        converter
                                .getConversionService()
                                .convert(row.get("r_zip_code", Object.class), String[].class))
                .orElse(new String[]{});

        String[] rAddressLine1s = Optional.ofNullable(
                        converter.getConversionService().convert(row.get("r_address_line1", Object.class), String[].class))
                .orElse(new String[]{});

        String[] rCountries = Optional.ofNullable(
                        converter.getConversionService().convert(row.get("r_country", Object.class), String[].class))
                .orElse(new String[]{});

        if (ObjectUtils.isEmpty(rIds)) return Collections.emptySet();

        return IntStream.range(0, rIds.length)
                .mapToObj(index -> Recipient.builder()
                        .id(rIds[index])
                        .firstName(rFirstNames[index])
                        .lastName(rLastNames[index])
                        .middleName(rMiddleNames[index])
                        .email(rEmails[index])
                        .mobilePhone(rMobilePhones[index])
                        .city(rCities[index])
                        .province(rProvinces[index])
                        .zipcode(rZipCodes[index])
                        .addressLine1(rAddressLine1s[index])
                        .country(rCountries[index])
                        .build()
                ).collect(Collectors.toSet());
    }

}

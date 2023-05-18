package com.example.graphqlexample.domain.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Table(name = "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Customer implements Serializable {
    @Id
    private String id;
    @Column(value = "first_name")
    private String firstName;
    @Column(value = "last_name")
    private String lastName;
    @Column(value = "middle_name")
    private String middleName;
    @Column(value = "external_provider_id")
    private String externalProviderId;
    private String email;
    private String gender;
    @Column(value = "address_line1")
    private String addressLine1;
    private String state;
    private String city;
    private String nationality;
    private String zipcode;
    private LocalDate dob;
    private String tier;
    @Column(value = "kyc_status")
    private String kycStatus;
    @Column(value = "mobile_phone")
    private String mobilePhone;
    @Column(value = "mobile_country_code")
    private String mobileCountryCode;
    @Builder.Default
    private Set<Recipient> recipients = new HashSet<>();

    @JsonIgnore
    public static Mono<Customer> fromRows(List<Map<String, Object>> rows) {
        return Mono.just(
                Customer.builder()
                        .id(rows.get(0).get("u_id").toString())
                        .firstName((String) rows.get(0).get("u_first_name"))
                        .middleName((String) rows.get(0).get("u_middle_name"))
                        .lastName((String) rows.get(0).get("u_last_name"))
                        .email((String) rows.get(0).get("u_email"))
                        .mobilePhone((String) rows.get(0).get("u_mobile_phone"))
                        .mobileCountryCode((String) rows.get(0).get("u_mobile_country_code"))
                        .gender((String) rows.get(0).get("u_gender"))
                        .city((String) rows.get(0).get("u_city"))
                        .state((String) rows.get(0).get("u_state"))
                        .addressLine1((String) rows.get(0).get("u_address_line1"))
                        .zipcode((String) rows.get(0).get("u_zipcode"))
                        .nationality((String) rows.get(0).get("u_nationality"))
                        .externalProviderId((String) rows.get(0).get("u_external_provider_id"))
                        .tier((String) rows.get(0).get("u_tier"))
                        .dob(LocalDate.parse(rows.get(0).get("u_dob").toString()))
                        .kycStatus((String) rows.get(0).get("u_kyc_status"))
                        .recipients(rows.stream()
                                .map(Recipient::fromRows)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toSet())
                        )

                        .build()
        );
    }
}

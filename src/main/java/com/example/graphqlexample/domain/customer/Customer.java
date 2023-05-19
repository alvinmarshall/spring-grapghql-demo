package com.example.graphqlexample.domain.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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
}

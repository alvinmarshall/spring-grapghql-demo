package com.example.graphqlexample.domain.configuration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Set;

@Table(name = "countries")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Country {
    @Id
    @Column("country_iso3_code")
    private String countryIso3code;
    @Column(value = "country_iso2_code")
    private String countryIso2code;
    private String name;
    @Column(value = "phone_code")
    private String phoneCode;
    @Column(value = "country_id")
    private String countryId;
    private String currency;
    private Set<State> states;
}

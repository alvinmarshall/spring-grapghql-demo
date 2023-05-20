package com.example.graphqlexample.domain.configuration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "states")
@Data
@Builder
@AllArgsConstructor
public class State {
    @Id
    private String id;
    private String name;
    private String code;
    @Column(value = "country_id")
    private Country country;
}

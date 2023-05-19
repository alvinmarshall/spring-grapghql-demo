package com.example.graphqlexample.domain.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;

@Table(name = "recipients")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Recipient implements Serializable {
    @Id
    private String id;
    @Column(value = "first_name")
    private String firstName;
    @Column(value = "last_name")
    private String lastName;
    @Column(value = "middle_name")
    private String middleName;
    private String email;
    @Column(value = "address_line1")
    private String addressLine1;
    private String province;
    private String country;
    private String city;
    @Column(value = "zip_code")
    private String zipcode;
    @Column(value = "mobile_phone")
    private String mobilePhone;
    private Customer customer;
}

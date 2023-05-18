package com.example.graphqlexample.domain.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.util.Map;

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
    @Column(value = "zip_code")
    private String zipcode;
    @Column(value = "mobile_phone")
    private String mobilePhone;
    private Customer customer;


    public static Recipient fromRows(Map<String, Object> row) {
        if (ObjectUtils.isEmpty(row.get("r_id"))) return null;
        return
                Recipient.builder()
                        .id((String) row.get("r_id"))
                        .firstName((String) row.get("r_first_name"))
                        .lastName((String) row.get("r_last_name"))
                        .middleName((String) row.get("r_middle_name"))
                        .email((String) row.get("r_email"))
                        .mobilePhone((String) row.get("r_mobile_phone"))
                        .addressLine1((String) row.get("r_address_line1"))
                        .country((String) row.get("r_country"))
                        .province((String) row.get("r_province"))
                        .zipcode((String) row.get("r_zip_code"))
                        .build();

    }
}

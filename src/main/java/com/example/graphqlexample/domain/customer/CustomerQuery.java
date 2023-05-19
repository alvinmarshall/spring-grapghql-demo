package com.example.graphqlexample.domain.customer;

public class CustomerQuery {
    private CustomerQuery() {
    }

    public static final String SELECT_CUSTOMER_WITH_RECIPIENTS = """
            SELECT
                u.id u_id,
                u.first_name u_first_name,
                u.last_name u_last_name,
                u.middle_name u_middle_name,
                u.email u_email,
                u.gender u_gender,
                u.mobile_phone u_mobile_phone,
                u.mobile_country_code u_mobile_country_code,
                u.city u_city,
                u.state u_state,
                u.zipcode u_zipcode,
                u.address_line1 u_address_line1,
                u.external_provider_id u_external_provider_id,
                u.nationality u_nationality,
                u.kyc_status u_kyc_status,
                u.tier u_tier,
                u.dob u_dob,
                
                ARRAY_AGG(r.id) as r_id,
                ARRAY_AGG(r.first_name) as r_first_name,
                ARRAY_AGG(r.last_name) as r_last_name,
                ARRAY_AGG(r.middle_name) as r_middle_name,
                ARRAY_AGG(r.email) as r_email,
                ARRAY_AGG(r.mobile_phone) as r_mobile_phone,
                ARRAY_AGG(r.city) as r_city,
                ARRAY_AGG(r.province) as r_province,
                ARRAY_AGG(r.zip_code) as r_zip_code,
                ARRAY_AGG(r.address_line1) as r_address_line1,
                ARRAY_AGG(r.country) as r_country
                
            FROM users u
            LEFT JOIN recipients r ON u.id = r.user_id
            WHERE u.id = :id
            GROUP BY u.id
            """;
}

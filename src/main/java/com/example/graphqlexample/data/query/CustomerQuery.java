package com.example.graphqlexample.data.query;

public class CustomerQuery {
    private CustomerQuery() {
    }

    public static String SELECT_QUERY = """
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
                
                
                r.id r_id,
                r.first_name r_first_name,
                r.last_name r_last_name,
                r.middle_name r_middle_name,
                r.email r_email,
                r.mobile_phone r_mobile_phone,
                r.city r_city,
                r.province r_province,
                r.zip_code r_zip_code,
                r.address_line1 r_address_line1,
                r.country r_country                
                
             FROM users u
                LEFT JOIN recipients r ON r.user_id = u.id
            """;
}

package com.example.graphqlexample.domain.configuration;

public class CountryQuery {
    private CountryQuery() {
    }

    public static final String SELECT_COUNTRY_WITH_STATES = """
            SELECT
                c.country_iso3_code c_country_iso3_code,
                c.country_iso2_code c_country_iso2_code,
                c.name c_name,
                c.phone_code c_phone_code,
                c.country_id c_country_id,
                
                ARRAY_AGG(s.id) as s_id,
                ARRAY_AGG(s.name) as s_name,
                ARRAY_AGG(s.code) as s_code
                
            FROM countries c
            LEFT JOIN states s ON c.country_iso3_code = s.country_id
            WHERE c.country_iso3_code = :iso3Code
            GROUP BY c.country_iso3_code
            """;
}

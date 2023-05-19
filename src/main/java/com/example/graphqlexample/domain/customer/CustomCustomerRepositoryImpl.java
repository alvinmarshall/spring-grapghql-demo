package com.example.graphqlexample.domain.customer;

import com.example.graphqlexample.mapper.customer.CustomerRowMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class CustomCustomerRepositoryImpl implements CustomCustomerRepository {
    private final DatabaseClient client;
    private final CustomerRowMapper customerRowMapper;

    public CustomCustomerRepositoryImpl(
            @Qualifier("customerDatabaseClient") DatabaseClient client,
            CustomerRowMapper customerRowMapper
    ) {
        this.client = client;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public Mono<Customer> findCustomerById(String id) {
        return client.sql(CustomerQuery.SELECT_CUSTOMER_WITH_RECIPIENTS)
                .bind("id", id)
                .map(customerRowMapper)
                .one();
    }
}

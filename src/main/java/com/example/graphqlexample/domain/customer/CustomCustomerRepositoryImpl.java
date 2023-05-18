package com.example.graphqlexample.domain.customer;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class CustomCustomerRepositoryImpl implements CustomCustomerRepository {
    private final DatabaseClient client;

    public CustomCustomerRepositoryImpl(@Qualifier("customerDatabaseClient") DatabaseClient client) {
        this.client = client;
    }

    @Override
    public Mono<Customer> findCustomerById(String id) {
        return client.sql("%s WHERE u.id = :id LIMIT 1".formatted(CustomerQuery.SELECT_QUERY))
                .bind("id", id)
                .fetch()
                .all()
                .bufferUntilChanged(result -> result.get("u_id"))
                .flatMap(Customer::fromRows)
                .singleOrEmpty();
    }
}

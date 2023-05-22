package com.example.graphqlexample.domain.customer;

import com.example.graphqlexample.dto.CountItem;
import com.example.graphqlexample.dto.customer.CountTier;
import com.example.graphqlexample.mapper.customer.CustomerRowMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

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

    @Override
    public Flux<Customer> findAllCustomers() {
        return client.sql(CustomerQuery.SELECT_CUSTOMERS)
                .map(customerRowMapper)
                .all();
    }

    @Override
    public Flux<CountItem> countCustomersNationalityByKycStatus(KycStatus status) {
        return client.sql(CustomerQuery.COUNT_CUSTOMERS_KYC_STATUS_NATIONALITY)
                .bind("kycStatus", status.name())
                .map((row, rowMetadata) -> {
                    Long count = row.get("count", Long.class);
                    String nationality = row.get("nationality", String.class);
                    return CountItem.builder().count(count).country(nationality).build();
                })
                .all();
    }

    @Override
    public Flux<CountItem> countCustomersStateByKycStatus(KycStatus status) {
        return client.sql(CustomerQuery.COUNT_CUSTOMERS_KYC_STATUS_STATE)
                .bind("kycStatus", status.name())
                .map((row, rowMetadata) -> {
                    Long count = row.get("count", Long.class);
                    String state = row.get("state", String.class);
                    return CountItem.builder().count(count).state(state).build();
                })
                .all();
    }

    @Override
    public Flux<CountTier> countCustomersTier() {
        return client.sql(CustomerQuery.COUNT_CUSTOMERS_TIER)
                .map((row, rowMetadata) -> {
                    Long count = row.get("count", Long.class);
                    String tier = Optional.ofNullable(row.get("tier", String.class)).orElse("N_A");
                    return CountTier.builder().count(count).tier(tier).build();
                })
                .all();
    }
}

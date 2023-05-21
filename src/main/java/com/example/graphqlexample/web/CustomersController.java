package com.example.graphqlexample.web;


import com.example.graphqlexample.domain.customer.Customer;
import com.example.graphqlexample.domain.customer.CustomerRepository;
import com.example.graphqlexample.domain.customer.RecipientRepository;
import com.example.graphqlexample.domain.transaction.TransactionRepository;
import com.example.graphqlexample.domain.transaction.TransactionStatus;
import com.example.graphqlexample.dto.CountItem;
import com.example.graphqlexample.dto.customer.CountRecipientCountry;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Controller
public class CustomersController {
    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;
    private final RecipientRepository recipientRepository;

    public CustomersController(
            CustomerRepository customerRepository,
            TransactionRepository transactionRepository,
            RecipientRepository recipientRepository
    ) {
        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
        this.recipientRepository = recipientRepository;
    }

    @QueryMapping
    public Flux<Customer> customers() {
        return customerRepository.findAll();
    }

    @QueryMapping
    public Mono<Customer> customer(@Argument String id) {
        return customerRepository.findCustomerById(id);
    }

    @QueryMapping
    public Mono<Long> totalTransaction(@Argument String customerId) {
        return transactionRepository.countByCustomer(customerId);
    }

    @QueryMapping
    public Mono<Long> totalTransactionByStatus(@Argument String customerId, @Argument TransactionStatus status) {
        return transactionRepository.countStatusByCustomer(status, customerId);
    }

    @BatchMapping(typeName = "Customer")
    public Flux<Long> totalTransactions(List<Customer> customers) {
        return Flux.fromStream(customers.stream())
                .concatMap(customer -> transactionRepository.countByCustomer(customer.getId()));
    }

    @BatchMapping(typeName = "Customer")
    public Flux<Long> totalTransactionsDelivered(List<Customer> customers) {
        return Flux.fromStream(customers.stream())
                .concatMap(customer ->
                        transactionRepository.countStatusByCustomer(TransactionStatus.DELIVERED, customer.getId()));
    }

    @BatchMapping(typeName = "Customer")
    public Flux<Long> totalTransactionsCancelled(List<Customer> customers) {
        return Flux.fromStream(customers.stream())
                .concatMap(customer ->
                        transactionRepository.countStatusByCustomer(TransactionStatus.CANCELLED, customer.getId()));
    }

    @BatchMapping(typeName = "Customer")
    public Flux<Long> totalTransactionsFailed(List<Customer> customers) {
        return Flux.fromStream(customers.stream())
                .concatMap(customer ->
                        transactionRepository.countStatusByCustomer(TransactionStatus.FAILED, customer.getId()));
    }

    @BatchMapping(typeName = "Customer")
    public Flux<List<CountRecipientCountry>> numberOfRecipientsOfEachCountry(List<Customer> customers) {
        return Flux.fromStream(customers.stream())
                .concatMap(customer -> recipientRepository.countRecipientsByCountry(customer.getId()).collectList());
    }

    @BatchMapping(typeName = "Customer")
    public Flux<List<CountItem>> numberOfTransactionsToEachCountry(List<Customer> customers) {
        return Flux.fromStream(customers.stream())
                .concatMap(customer -> transactionRepository
                        .findTransactionByCustomer(customer.getId())
                        .concatMap(transaction -> {
                            String recipientId = transaction.getRecipient().getId();
                            return recipientRepository.findById(recipientId)
                                    .map(recipient -> CountItem.builder().count(1L)
                                            .country(recipient.getCountry())
                                            .build()
                                    );
                        })
                        .groupBy(CountItem::getCountry)
                        .concatMap(groupedFlux -> groupedFlux.reduce((countItem, countItem2) -> {
                            long newCount = countItem.getCount() + countItem2.getCount();
                            return CountItem.builder().count(newCount).country(countItem.getCountry()).build();
                        }))
                        .collectList()
                );
    }
}

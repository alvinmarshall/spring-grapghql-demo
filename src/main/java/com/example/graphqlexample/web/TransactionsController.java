package com.example.graphqlexample.web;


import com.example.graphqlexample.domain.transaction.Transaction;
import com.example.graphqlexample.domain.transaction.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

@Controller
@Slf4j
public class TransactionsController {
    private final TransactionRepository transactionRepository;

    public TransactionsController(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }


    @QueryMapping
    public Flux<Transaction> transactions(@Argument String customerId) {
        return transactionRepository.findTransactionByCustomer(customerId);
    }


}

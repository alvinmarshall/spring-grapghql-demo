package com.example.graphqlexample.web;

import com.example.graphqlexample.domain.configuration.Country;
import com.example.graphqlexample.domain.configuration.CountryRepository;
import com.example.graphqlexample.domain.configuration.State;
import com.example.graphqlexample.domain.configuration.StateRepository;
import com.example.graphqlexample.domain.customer.RecipientRepository;
import com.example.graphqlexample.domain.transaction.TransactionRepository;
import com.example.graphqlexample.domain.transaction.TransactionType;
import com.example.graphqlexample.dto.CountItem;
import com.example.graphqlexample.dto.transaction.SumAmountRecipientState;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.List;
import java.util.Objects;

@Controller
public class ConfigurationsController {
    private final CountryRepository countryRepository;
    private final StateRepository stateRepository;
    private final TransactionRepository transactionRepository;
    private final RecipientRepository recipientRepository;

    public ConfigurationsController(
            CountryRepository countryRepository,
            StateRepository stateRepository,
            TransactionRepository transactionRepository,
            RecipientRepository recipientRepository
    ) {
        this.countryRepository = countryRepository;
        this.stateRepository = stateRepository;
        this.transactionRepository = transactionRepository;
        this.recipientRepository = recipientRepository;
    }

    @QueryMapping
    public Flux<Country> countries() {
        return countryRepository.findAll();
    }

    @QueryMapping
    public Mono<Country> countryByIso3Code(@Argument String iso3Code) {
        return countryRepository.findByIdWithStates(iso3Code);
    }

    @BatchMapping(typeName = "Country")
    public Flux<List<State>> states(List<Country> countries) {
        return Flux.fromStream(countries.stream())
                .concatMap(country -> stateRepository.findByCountry_CountryId(country.getCountryIso3code()).collectList());
    }

    @BatchMapping(typeName = "State")
    public Flux<List<CountItem>> totalTransaction(List<State> states) {
        return Flux.fromStream(states.stream())
                .concatMap(state -> transactionRepository.countAndGroupByRecipients()
                        .concatMap(countByRecipient ->
                                recipientRepository
                                        .findById(countByRecipient.getRecipientId())
                                        .filter(recipient -> recipient.getProvince().equals(state.getName()))
                                        .filter(Objects::nonNull)
                                        .map(recipient -> CountItem.builder()
                                                .count(countByRecipient.getCount())
                                                .state(recipient.getProvince())
                                                .build()))
                        .collectList());
    }

    @BatchMapping(typeName = "State")
    public Flux<List<CountItem>> numberOfACHTransactionState(List<State> states) {
        return Flux.fromStream(states.stream())
                .concatMap(state -> transactionRepository.countTypeAndGroupByRecipients(TransactionType.ACH)
                        .concatMap(countByRecipient ->
                                recipientRepository
                                        .findById(countByRecipient.getRecipientId())
                                        .filter(recipient -> recipient.getProvince().equals(state.getName()))
                                        .filter(Objects::nonNull)
                                        .map(recipient -> CountItem.builder()
                                                .count(countByRecipient.getCount())
                                                .state(recipient.getProvince())
                                                .build()))
                        .collectList());

    }

    @BatchMapping(typeName = "State")
    public Flux<List<CountItem>> numberOfInstantTransactionState(List<State> states) {
        return Flux.fromStream(states.stream())
                .concatMap(state -> transactionRepository.countTypeAndGroupByRecipients(TransactionType.INSTANT)
                        .concatMap(countByRecipient ->
                                recipientRepository
                                        .findById(countByRecipient.getRecipientId())
                                        .filter(recipient -> recipient.getProvince().equals(state.getName()))
                                        .filter(Objects::nonNull)
                                        .map(recipient -> CountItem.builder()
                                                .count(countByRecipient.getCount())
                                                .state(recipient.getProvince())
                                                .build()))
                        .collectList());
    }

    @BatchMapping(typeName = "State")
    public Flux<List<SumAmountRecipientState>> totalAmountOfTransactionState(List<State> states) {
        return Flux.fromStream(states.stream())
                .concatMap(state -> transactionRepository.sumFromAmountAndGroupByRecipients()
                        .concatMap(countByRecipient ->
                                recipientRepository
                                        .findById(countByRecipient.getRecipientId())
                                        .filter(recipient -> recipient.getProvince().equals(state.getName()))
                                        .filter(Objects::nonNull)
                                        .map(recipient -> SumAmountRecipientState.builder()
                                                .amount(countByRecipient.getAmount())
                                                .state(recipient.getProvince())
                                                .currency(countByRecipient.getCurrency())
                                                .build()))
                        .collectList());
    }

    @BatchMapping(typeName = "Country")
    public Flux<List<SumAmountRecipientState>> totalAmountOfTransactionCountry(List<Country> countries) {
        return Flux.fromStream(countries.stream())
                .concatMap(country -> transactionRepository.sumFromAmountAndGroupByRecipients()
                        .concatMap(countByRecipient ->
                                recipientRepository
                                        .findById(countByRecipient.getRecipientId())
                                        .filter(recipient -> recipient.getCountry().equals(country.getCountryIso3code()))
                                        .filter(Objects::nonNull)
                                        .map(recipient -> SumAmountRecipientState.builder()
                                                .amount(countByRecipient.getAmount())
                                                .country(recipient.getCountry())
                                                .currency(countByRecipient.getCurrency())
                                                .build()))
                        .groupBy(SumAmountRecipientState::getCountry)
                        .flatMap(groupedFlux -> groupedFlux.reduce((smAmount1, sumAmount2) -> {
                            BigDecimal newAmount = smAmount1.getAmount().add(sumAmount2.getAmount());
                            //https://stackoverflow.com/questions/1359817/using-bigdecimal-to-work-with-currencies
                            int scale = Currency.getInstance(smAmount1.getCurrency()).getDefaultFractionDigits();
                            newAmount = newAmount.setScale(scale, RoundingMode.HALF_EVEN);
                            return SumAmountRecipientState.builder()
                                    .amount(newAmount)
                                    .currency(smAmount1.getCurrency())
                                    .country(smAmount1.getCountry()).build();
                        }))
                        .collectList());

    }

    @BatchMapping(typeName = "Country")
    public Flux<List<CountItem>> numberOfACHTransactionCountry(List<Country> countries) {
        return Flux.fromStream(countries.stream())
                .concatMap(country -> transactionRepository.countTypeAndGroupByRecipients(TransactionType.ACH)
                        .concatMap(countByRecipient ->
                                recipientRepository
                                        .findById(countByRecipient.getRecipientId())
                                        .filter(recipient -> recipient.getCountry().equals(country.getCountryIso3code()))
                                        .filter(Objects::nonNull)
                                        .map(recipient -> CountItem.builder()
                                                .count(countByRecipient.getCount())
                                                .country(recipient.getCountry())
                                                .build()))
                        .groupBy(CountItem::getCountry)
                        .flatMap(groupedFlux -> groupedFlux.reduce((countR1, countR2) -> {
                            long newCount = countR1.getCount() + countR2.getCount();
                            return CountItem.builder().count(newCount).country(countR1.getCountry()).build();
                        }))
                        .collectList());

    }

    @BatchMapping(typeName = "Country")
    public Flux<List<CountItem>> numberOfInstantTransactionCountry(List<Country> countries) {
        return Flux.fromStream(countries.stream())
                .concatMap(country -> transactionRepository.countTypeAndGroupByRecipients(TransactionType.INSTANT)
                        .concatMap(countByRecipient ->
                                recipientRepository
                                        .findById(countByRecipient.getRecipientId())
                                        .filter(recipient -> recipient.getCountry().equals(country.getCountryIso3code()))
                                        .filter(Objects::nonNull)
                                        .map(recipient -> CountItem.builder()
                                                .count(countByRecipient.getCount())
                                                .country(recipient.getCountry())
                                                .build()))
                        .groupBy(CountItem::getCountry)
                        .flatMap(groupedFlux -> groupedFlux.reduce((countR1, countR2) -> {
                            long newCount = countR1.getCount() + countR2.getCount();
                            return CountItem.builder().count(newCount).country(countR1.getCountry()).build();
                        }))
                        .collectList());
    }
}

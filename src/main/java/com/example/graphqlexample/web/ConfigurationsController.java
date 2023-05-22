package com.example.graphqlexample.web;

import com.example.graphqlexample.domain.configuration.Country;
import com.example.graphqlexample.domain.configuration.CountryRepository;
import com.example.graphqlexample.domain.configuration.State;
import com.example.graphqlexample.domain.configuration.StateRepository;
import com.example.graphqlexample.domain.customer.CustomerRepository;
import com.example.graphqlexample.domain.customer.KycStatus;
import com.example.graphqlexample.domain.customer.RecipientRepository;
import com.example.graphqlexample.domain.transaction.TransactionRepository;
import com.example.graphqlexample.domain.transaction.TransactionType;
import com.example.graphqlexample.dto.CountItem;
import com.example.graphqlexample.dto.transaction.SumAmountRecipientState;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@Slf4j
public class ConfigurationsController {
    private final CountryRepository countryRepository;
    private final StateRepository stateRepository;
    private final TransactionRepository transactionRepository;
    private final RecipientRepository recipientRepository;
    private final CustomerRepository customerRepository;

    public ConfigurationsController(
            CountryRepository countryRepository,
            StateRepository stateRepository,
            TransactionRepository transactionRepository,
            RecipientRepository recipientRepository,
            CustomerRepository customerRepository) {
        this.countryRepository = countryRepository;
        this.stateRepository = stateRepository;
        this.transactionRepository = transactionRepository;
        this.recipientRepository = recipientRepository;
        this.customerRepository = customerRepository;
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
        Set<String> stateSet = states.stream().map(State::getCode).collect(Collectors.toSet());
        Flux<CountItem> countItemFlux = transactionRepository.countAndGroupByRecipients()
                .flatMap(countByRecipient ->
                        recipientRepository
                                .findById(countByRecipient.getRecipientId())
                                .filter(recipient -> stateSet.contains(recipient.getProvince()))
                                .filter(Objects::nonNull)
                                .map(recipient -> CountItem.builder()
                                        .count(countByRecipient.getCount())
                                        .state(recipient.getProvince())
                                        .build()));
        return Flux.fromStream(states.stream())
                .concatMap(state -> countItemFlux
                        .filter(countItem -> state.getCode().equals(countItem.getState()))
                        .collectList());
    }

    @BatchMapping(typeName = "State")
    public Flux<List<CountItem>> numberOfACHTransactionState(List<State> states) {
        Set<String> stateSet = states.stream().map(State::getCode).collect(Collectors.toSet());
        Flux<CountItem> countItemFlux = transactionRepository.countTypeAndGroupByRecipients(TransactionType.ACH)
                .flatMap(countByRecipient ->
                        recipientRepository
                                .findById(countByRecipient.getRecipientId())
                                .filter(recipient -> stateSet.contains(recipient.getProvince()))
                                .filter(Objects::nonNull)
                                .map(recipient -> CountItem.builder()
                                        .count(countByRecipient.getCount())
                                        .state(recipient.getProvince())
                                        .build()));
        return Flux.fromIterable(states)
                .concatMap(state -> countItemFlux
                        .filter(countItem -> state.getCode().equals(countItem.getState())).collectList());

    }

    @BatchMapping(typeName = "State")
    public Flux<List<CountItem>> numberOfInstantTransactionState(List<State> states) {
        Set<String> stateSet = states.stream().map(State::getCode).collect(Collectors.toSet());
        Flux<CountItem> countItemFlux = transactionRepository
                .countTypeAndGroupByRecipients(TransactionType.INSTANT)
                .flatMap(countByRecipient ->
                        recipientRepository
                                .findById(countByRecipient.getRecipientId())
                                .filter(recipient -> stateSet.contains(recipient.getProvince()))
                                .filter(Objects::nonNull)
                                .map(recipient -> CountItem.builder()
                                        .count(countByRecipient.getCount())
                                        .state(recipient.getProvince())
                                        .build()));
        return Flux.fromIterable(states)
                .concatMap(state -> countItemFlux
                        .filter(countItem -> state.getCode().equals(countItem.getState())).collectList());

    }

    @BatchMapping(typeName = "State")
    public Flux<List<SumAmountRecipientState>> totalAmountOfTransactionState(List<State> states) {
        Set<String> stateSet = states.stream().map(State::getCode).collect(Collectors.toSet());
        Flux<SumAmountRecipientState> recipientStateFlux = transactionRepository.sumFromAmountAndGroupByRecipients()
                .flatMap(countByRecipient ->
                        recipientRepository
                                .findById(countByRecipient.getRecipientId())
                                .filter(recipient -> stateSet.contains(recipient.getProvince()))
                                .filter(Objects::nonNull)
                                .map(recipient -> SumAmountRecipientState.builder()
                                        .amount(countByRecipient.getAmount())
                                        .state(recipient.getProvince())
                                        .currency(countByRecipient.getCurrency())
                                        .build()));
        return Flux.fromIterable(states)
                .concatMap(state -> recipientStateFlux.
                        filter(smAmountState -> state.getCode().equals(smAmountState.getState()))
                        .collectList());
    }

    @BatchMapping(typeName = "State")
    public Flux<List<CountItem>> numberOfKycVerifiedCustomersState(List<State> states) {
        Set<String> stateSet = states.stream().map(State::getCode).collect(Collectors.toSet());
        Flux<CountItem> countItemFlux = customerRepository
                .countCustomersStateByKycStatus(KycStatus.VERIFIED)
                .filter(countItem -> stateSet.contains(countItem.getState()));
        return Flux.fromIterable(states)
                .concatMap(state -> countItemFlux
                        .filter(countItem -> state.getCode().equals(countItem.getState())).collectList()
                );
    }

    @BatchMapping(typeName = "State")
    public Flux<List<CountItem>> numberOfKycUnVerifiedCustomersState(List<State> states) {
        Set<String> stateSet = states.stream().map(State::getCode).collect(Collectors.toSet());
        Flux<CountItem> countItemFlux = customerRepository
                .countCustomersStateByKycStatus(KycStatus.UNVERIFIED)
                .filter(countItem -> stateSet.contains(countItem.getState()));
        return Flux.fromIterable(states)
                .concatMap(state -> countItemFlux
                        .filter(countItem -> state.getCode().equals(countItem.getState())).collectList()
                );
    }


    @BatchMapping(typeName = "Country")
    public Flux<List<SumAmountRecipientState>> totalAmountOfTransactionCountry(List<Country> countries) {
        Set<String> countrySet = countries.stream().map(Country::getCountryIso3code).collect(Collectors.toSet());
        Flux<SumAmountRecipientState> recipientStateFlux = transactionRepository.sumFromAmountAndGroupByRecipients()
                .flatMap(countByRecipient ->
                        recipientRepository
                                .findById(countByRecipient.getRecipientId())
                                .filter(recipient -> countrySet.contains(recipient.getCountry()))
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
                }));
        return Flux.fromIterable(countries)
                .concatMap(country -> recipientStateFlux
                        .filter(smAmount -> country.getCountryIso3code().equals(smAmount.getCountry())).collectList());

    }

    @BatchMapping(typeName = "Country")
    public Flux<List<CountItem>> numberOfACHTransactionCountry(List<Country> countries) {
        Set<String> countrySet = countries.stream().map(Country::getCountryIso3code).collect(Collectors.toSet());
        Flux<CountItem> countItemFlux = transactionRepository.countTypeAndGroupByRecipients(TransactionType.ACH)
                .flatMap(countByRecipient ->
                        recipientRepository
                                .findById(countByRecipient.getRecipientId())
                                .filter(recipient -> countrySet.contains(recipient.getCountry()))
                                .filter(Objects::nonNull)
                                .map(recipient -> CountItem.builder()
                                        .count(countByRecipient.getCount())
                                        .country(recipient.getCountry())
                                        .build()))
                .groupBy(CountItem::getCountry)
                .flatMap(groupedFlux -> groupedFlux.reduce((countR1, countR2) -> {
                    long newCount = countR1.getCount() + countR2.getCount();
                    return CountItem.builder().count(newCount).country(countR1.getCountry()).build();
                }));
        return Flux.fromIterable(countries)
                .concatMap(country -> countItemFlux.
                        filter(countItem -> countItem.getCountry().equals(country.getCountryIso3code()))
                        .collectList());

    }

    @BatchMapping(typeName = "Country")
    public Flux<List<CountItem>> numberOfInstantTransactionCountry(List<Country> countries) {
        Set<String> countrySet = countries.stream().map(Country::getCountryIso3code).collect(Collectors.toSet());
        Flux<CountItem> countItemFlux = transactionRepository.countTypeAndGroupByRecipients(TransactionType.INSTANT)
                .flatMap(countByRecipient ->
                        recipientRepository
                                .findById(countByRecipient.getRecipientId())
                                .filter(recipient -> countrySet.contains(recipient.getCountry()))
                                .filter(Objects::nonNull)
                                .map(recipient -> CountItem.builder()
                                        .count(countByRecipient.getCount())
                                        .country(recipient.getCountry())
                                        .build()))
                .groupBy(CountItem::getCountry)
                .flatMap(groupedFlux -> groupedFlux.reduce((countR1, countR2) -> {
                    long newCount = countR1.getCount() + countR2.getCount();
                    return CountItem.builder().count(newCount).country(countR1.getCountry()).build();
                }));
        return Flux.fromIterable(countries)
                .concatMap(country -> countItemFlux.
                        filter(countItem -> countItem.getCountry().equals(country.getCountryIso3code()))
                        .collectList());
    }

    @BatchMapping(typeName = "Country")
    public Flux<List<CountItem>> numberOfKycVerifiedCustomersCountry(List<Country> countries) {
        Set<String> countrySet = countries.stream().map(Country::getCountryIso3code).collect(Collectors.toSet());
        Flux<CountItem> countItemFlux = customerRepository
                .countCustomersNationalityByKycStatus(KycStatus.VERIFIED)
                .filter(countItem -> countrySet.contains(countItem.getCountry()));
        return Flux.fromIterable(countries)
                .concatMap(country -> countItemFlux
                        .filter(countItem -> country.getCountryIso3code().equals(countItem.getCountry())).collectList()
                );

    }

    @BatchMapping(typeName = "Country")
    public Flux<List<CountItem>> numberOfKycUnVerifiedCustomersCountry(List<Country> countries) {
        Set<String> countrySet = countries.stream().map(Country::getCountryIso3code).collect(Collectors.toSet());
        Flux<CountItem> countItemFlux = customerRepository
                .countCustomersNationalityByKycStatus(KycStatus.UNVERIFIED)
                .filter(countItem -> countrySet.contains(countItem.getCountry()));
        return Flux.fromIterable(countries)
                .concatMap(country -> countItemFlux
                        .filter(countItem -> country.getCountryIso3code().equals(countItem.getCountry())).collectList()
                );
    }
}

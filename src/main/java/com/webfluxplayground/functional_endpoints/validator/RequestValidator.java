package com.webfluxplayground.functional_endpoints.validator;

import com.webfluxplayground.functional_endpoints.dto.CustomerDto;
import com.webfluxplayground.functional_endpoints.exception.ApplicationExceptions;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class RequestValidator {

    public static UnaryOperator<Mono<CustomerDto>> validate() {
        return mono -> mono.filter(hasName())
                .switchIfEmpty(ApplicationExceptions.missingNameMono())
                .filter(hasValidEmail())
                .switchIfEmpty(ApplicationExceptions.missingEmailMono());
    }

    public static void validate(CustomerDto customerDto) {
        if(!Objects.nonNull(customerDto.name())){
            ApplicationExceptions.missingName();
        }
        if(!(Objects.nonNull(customerDto.email()) && customerDto.email().contains("@"))){
            ApplicationExceptions.missingEmail();
        }
    }

    private static Predicate<CustomerDto> hasName() {
        return dto -> Objects.nonNull(dto.name());
    }

    private static Predicate<CustomerDto> hasValidEmail() {
        return dto -> Objects.nonNull(dto.email()) && dto.email().contains("@"); //simple validation
    }
}

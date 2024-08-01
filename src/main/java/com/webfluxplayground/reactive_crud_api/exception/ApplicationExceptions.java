package com.webfluxplayground.reactive_crud_api.exception;

import reactor.core.publisher.Mono;

public class ApplicationExceptions {

    public static <T> Mono<T> customerNotFoundMono(int id){
        return Mono.error(new CustomerNotFoundException(id));
    }

    public static <T> Mono<T> missingNameMono(){
        return Mono.error(new InvalidInputException("Name is required"));
    }

    public static <T> Mono<T> missingEmailMono(){
        return Mono.error(new InvalidInputException("Email is required"));
    }

    public static void customerNotFound(int id){
        throw new CustomerNotFoundException(id);
    }

    public static void missingName(){
        throw new InvalidInputException("Name is required");
    }

    public static <T> Mono<T> missingEmail(){
        throw new InvalidInputException("Email is required");
    }
}

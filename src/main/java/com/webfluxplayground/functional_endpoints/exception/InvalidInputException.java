package com.webfluxplayground.functional_endpoints.exception;

public class InvalidInputException extends RuntimeException {

    public InvalidInputException(String message) {
        super(message);
    }
}

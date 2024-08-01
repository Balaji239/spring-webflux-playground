package com.webfluxplayground.reactive_crud_api.exception;

public class CustomerNotFoundException extends RuntimeException {

    private static final String MESSAGE = "Customer with [id=%d] not found";

    public CustomerNotFoundException(int id) {
        super(MESSAGE.formatted(id));
    }
}

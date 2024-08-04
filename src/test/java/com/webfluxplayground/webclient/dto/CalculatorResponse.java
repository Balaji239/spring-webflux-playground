package com.webfluxplayground.webclient.dto;

public record CalculatorResponse(int first,
                                 int second,
                                 String operation,
                                 double result) {
}
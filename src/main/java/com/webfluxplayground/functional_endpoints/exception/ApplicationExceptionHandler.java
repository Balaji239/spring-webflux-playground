package com.webfluxplayground.functional_endpoints.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.function.Consumer;

@Component
public class ApplicationExceptionHandler {

    public Mono<ServerResponse> handleException(CustomerNotFoundException ex, ServerRequest request){
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, request, (problemDetail)-> {
            problemDetail.setType(URI.create("http://example.com/problem/customer-not-found"));
            problemDetail.setTitle("Customer not found");
        });
    }

    public Mono<ServerResponse> handleException(InvalidInputException ex, ServerRequest request){
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, request, (problemDetail)-> {
            problemDetail.setType(URI.create("http://example.com/problem/invalid-input"));
            problemDetail.setTitle("Invalid input");
        });
    }

    public Mono<ServerResponse> handleException(RuntimeException ex, ServerRequest request){
        return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, request, (problemDetail)-> {
            problemDetail.setTitle("Internal server error. Please try after sometime");
        });
    }

    private Mono<ServerResponse> buildErrorResponse(Exception exception, HttpStatus httpStatus, ServerRequest request, Consumer<ProblemDetail> consumer){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(httpStatus, exception.getMessage());
        consumer.accept(problemDetail);
        problemDetail.setInstance(URI.create(request.path()));
        return ServerResponse.status(httpStatus).bodyValue(problemDetail);
    }

}

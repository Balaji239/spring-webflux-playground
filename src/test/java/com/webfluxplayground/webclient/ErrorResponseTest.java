package com.webfluxplayground.webclient;

import com.webfluxplayground.util.Util;
import com.webfluxplayground.webclient.dto.CalculatorResponse;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ProblemDetail;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

public class ErrorResponseTest extends AbstractWebClient {

    private static final Logger log = LoggerFactory.getLogger(ErrorResponseTest.class);
    private final WebClient webClient = getWebClient();

    @Test
    void errorHandling() {
        webClient.get()
                .uri("/lec05/calculator/{a}/{b}", 10, 20)
                .header("operation", "@")
                .retrieve()
                .bodyToMono(CalculatorResponse.class)
                //.doOnError(WebClientResponseException.class, ex -> log.info("{}", ex.getResponseBodyAs(ProblemDetail.class)))
                // sending some default response in case of error
                .onErrorReturn(WebClientResponseException.InternalServerError.class, new CalculatorResponse(0, 0, null, 0.0))
                .onErrorReturn(WebClientResponseException.BadRequest.class, new CalculatorResponse(0, 0, null, -1.0))
                .doOnNext((response) -> log.info("received {}", response))
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    @Test
    void errorHandlingWithExchange() {
        webClient.get()
                .uri("/lec05/calculator/{a}/{b}", 10, 20)
                .header("operation", "#")
                .exchangeToMono(clientResponse -> decode(clientResponse))
                .doOnNext((response) -> log.info("received {}", response))
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    private Mono<Object> decode(ClientResponse clientResponse){
        if(clientResponse.statusCode().isError()){
            return clientResponse.bodyToMono(ProblemDetail.class)
                    .flatMap(problemDetail -> Mono.just(problemDetail));
        }
        return clientResponse.bodyToMono(CalculatorResponse.class);
    }
}

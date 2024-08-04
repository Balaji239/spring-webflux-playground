package com.webfluxplayground.webclient;

import com.webfluxplayground.webclient.dto.CalculatorResponse;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.util.Map;

public class QueryParamsTest extends AbstractWebClient {

    private static final Logger log = LoggerFactory.getLogger(QueryParamsTest.class);
    private final WebClient webClient = getWebClient();

    @Test
    public void uriBuilderVariables() {
        var path = "/lec06/calculator";
        var query = "first={first}&second={second}&operation={operation}";
        webClient.get()
                .uri(builder -> builder.path(path).query(query).build(10, 20, "+"))
                .retrieve()
                .bodyToMono(CalculatorResponse.class)
                .doOnNext((resp) -> log.info("response {}", resp))
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    @Test
    public void uriBuilderMap() {
        String path = "/lec06/calculator";
        String query = "first={first}&second={second}&operation={operation}";
        var map = Map.of(
                "first", 10,
                "second", 20,
                "operation", "*"
        );
        webClient.get()
                .uri(builder -> builder.path(path).query(query).build(map))
                .retrieve()
                .bodyToMono(CalculatorResponse.class)
                .doOnNext((resp) -> log.info("response {}", resp))
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }
}

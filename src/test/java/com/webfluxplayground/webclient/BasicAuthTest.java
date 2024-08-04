package com.webfluxplayground.webclient;

import com.webfluxplayground.webclient.dto.Product;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

public class BasicAuthTest extends AbstractWebClient{

    private static final Logger log = LoggerFactory.getLogger(BasicAuthTest.class);
    private final WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:7070/demo02")
            .defaultHeaders(headers -> headers.setBasicAuth("java", "secret"))
            .build();

    @Test
    public void basicAuth() {
        webClient.get()
                .uri("/lec07/product/{id}", 1)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext((response) -> log.info("received {}", response))
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }
}

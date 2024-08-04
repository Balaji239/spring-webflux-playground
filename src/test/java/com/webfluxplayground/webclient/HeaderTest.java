package com.webfluxplayground.webclient;

import com.webfluxplayground.util.Util;
import com.webfluxplayground.webclient.dto.Product;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.Map;

public class HeaderTest extends AbstractWebClient {

    private static final Logger log = LoggerFactory.getLogger(HeaderTest.class);
    private final WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:7070/demo02")
            .defaultHeader("caller-id", "my-service").build();

    @Test
    public void defaultHeader() {
        webClient.get()
                .uri("/lec04/product/{id}", 1)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext((response) -> log.info("received {}", response))
                .subscribe();
        Util.threadSleep(Duration.ofSeconds(2));
    }

    @Test
    public void overrideHeader() {
        webClient.get()
                .uri("/lec04/product/{id}", 1)
                .header("caller-id", "new-value")
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext((response) -> log.info("received {}", response))
                .subscribe();
        Util.threadSleep(Duration.ofSeconds(2));
    }

    @Test
    public void headersWithMap() {
        var map = Map.of(
                "caller-id", "new-value",
                "some-key", "some-value"
        );
        webClient.get()
                .uri("/lec04/product/{id}", 1)
                .headers(h -> h.setAll(map))
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext((response) -> log.info("received {}", response))
                .subscribe();
        Util.threadSleep(Duration.ofSeconds(2));
    }
}

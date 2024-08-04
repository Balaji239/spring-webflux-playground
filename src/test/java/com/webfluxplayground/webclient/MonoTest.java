package com.webfluxplayground.webclient;

import com.webfluxplayground.util.Util;
import com.webfluxplayground.webclient.dto.Product;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

public class MonoTest extends AbstractWebClient {

    private static final Logger log = LoggerFactory.getLogger(MonoTest.class);
    private final WebClient webClient = getWebClient();

    @Test
    void getRequest() {
        webClient.get()
                .uri("/lec01/product/1")
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext((item) -> log.info("received {}", item))
                .subscribe();

        Util.threadSleep(Duration.ofSeconds(2));
    }

    @Test
    void concurrentNonBlockingRequest() {
        for (int i = 1; i <= 50; i++) {
            webClient.get()
                    .uri("/lec01/product/{id}",i)
                    .retrieve()
                    .bodyToMono(Product.class)
                    .doOnNext((item) -> log.info("received {}", item))
                    .subscribe();
        }
        Util.threadSleep(Duration.ofSeconds(2));
    }
}

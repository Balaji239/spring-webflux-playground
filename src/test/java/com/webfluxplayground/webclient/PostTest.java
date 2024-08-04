package com.webfluxplayground.webclient;

import com.webfluxplayground.util.Util;
import com.webfluxplayground.webclient.dto.Product;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

public class PostTest extends AbstractWebClient {

    private static final Logger log = LoggerFactory.getLogger(PostTest.class);
    private final WebClient webClient = getWebClient();

    @Test
    void postBodyValue() {
        Product product = new Product(0, "PS5", 20000);
        webClient.post()
                .uri("/lec03/product")
                .bodyValue(product)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext((response) -> log.info("received {}", response))
                .subscribe();
        Util.threadSleep(Duration.ofSeconds(2));
    }

    @Test
    void postBody() {
        Mono<Product> productMono = Mono.fromSupplier(() -> new Product(0, "PS5", 20000));
        webClient.post()
                .uri("/lec03/product")
                .body(productMono, Product.class)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext((response) -> log.info("received {}", response))
                .subscribe();
        Util.threadSleep(Duration.ofSeconds(2));
    }
}

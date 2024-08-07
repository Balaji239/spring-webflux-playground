package com.webfluxplayground.server_sent_events;

import com.webfluxplayground.server_sent_events.dto.ProductDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

@AutoConfigureWebTestClient
@SpringBootTest
public class ProductStreamingTest {

    private static final Logger log = LoggerFactory.getLogger(ProductStreamingTest.class);

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testProductStreaming(){
        webTestClient.get()
                .uri("/products/stream/60")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .returnResult(ProductDto.class)
                .getResponseBody()
                .take(3)
                .doOnNext(productDto -> log.info("received: {}", productDto))
                .collectList()
                .as(StepVerifier::create)
                .assertNext(list -> {
                    Assertions.assertEquals(3, list.size());
                    Assertions.assertTrue(list.stream().allMatch(productDto -> productDto.price()<=60));
                })
                .expectComplete()
                .verify();
    }
}

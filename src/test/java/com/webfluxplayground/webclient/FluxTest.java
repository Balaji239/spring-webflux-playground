package com.webfluxplayground.webclient;

import com.webfluxplayground.util.Util;
import com.webfluxplayground.webclient.dto.Product;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

public class FluxTest extends AbstractWebClient{

    private static final Logger log = LoggerFactory.getLogger(FluxTest.class);
    private final WebClient webClient = getWebClient();

    @Test
    void getFluxData(){
        webClient.get()
                .uri("/lec02/product/stream")
                .retrieve()
                .bodyToFlux(Product.class)
                .doOnNext((item)-> log.info("received {}", item))
                .subscribe();

        Util.threadSleep(Duration.ofSeconds(3));
    }
}

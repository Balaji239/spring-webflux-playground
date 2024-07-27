package com.webfluxplayground.traditional_vs_reactive_api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.awt.*;

@RestController
@RequestMapping("/reactive")
public class ReactiveWebController {
    
    private static final Logger log = LoggerFactory.getLogger(ReactiveWebController.class);
    private final WebClient webClient = WebClient.builder().baseUrl("http://localhost:7070").build();

    @GetMapping(value = "/products", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Product> getProducts(){
        Flux<Product> productFlux = webClient.get()
                .uri("demo01/products")
                .retrieve()
                .bodyToFlux(Product.class)
                .doOnNext(p -> log.info("received: {}", p));
        return productFlux;
    }

    // this external endpoint will fail after sending some products info
    // since this is a reactive endpoint, whatever response we got till failure we can have it
    // the only problem is the json response will not be in proper format ex: closing of } ] will not be proper as its a partial response
    // to handle this we can just use the onErrorComplete operator. This will make the response to be in proper format now.
    // due to this capabilities we call reactive web as resilient
    @GetMapping(value = "/products/notorious", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Product> getProducts2(){
        Flux<Product> productFlux = webClient.get()
                .uri("demo01/products/notorious")
                .retrieve()
                .bodyToFlux(Product.class)
                .onErrorComplete()
                .doOnNext(p -> log.info("received: {}", p));
        return productFlux;
    }
}

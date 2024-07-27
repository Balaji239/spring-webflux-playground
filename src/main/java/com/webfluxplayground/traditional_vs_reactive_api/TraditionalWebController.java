package com.webfluxplayground.traditional_vs_reactive_api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/traditional")
public class TraditionalWebController {

    private static final Logger log = LoggerFactory.getLogger(TraditionalWebController.class);
    private final RestClient restClient = RestClient.builder().baseUrl("http://localhost:7070").build();

    @GetMapping("/products")
    public List<Product> getProducts() {
        List<Product> products = restClient.get()
                .uri("demo01/products")
                .retrieve()
                .body(new ParameterizedTypeReference<List<Product>>() {
                });
        log.info("received response: {}", products);
        return products;
    }

    // dont do this way, this is not reactive programming as we are still doing a synchronous blocking call here
    //@GetMapping("/products")
    //public Flux<Product> getProducts2() {
    //    List<Product> products = restClient.get()
    //            .uri("demo01/products")
    //            .retrieve()
    //            .body(new ParameterizedTypeReference<List<Product>>() {
    //            });
    //    log.info("received response: {}", products);
    //    return Flux.fromIterable(products);
    //}

    // this external endpoint will fail after sending some products info
    // this means we cannot get the partial response in case of traditional web endpoint. Its like all or nothing
    // we can handle the error and send some empty list or dummy data, but actual products response for some products are lost.
    @GetMapping("/products/notorious")
    public List<Product> getProducts3() {
        List<Product> products = restClient.get()
                .uri("demo01/products/notorious")
                .retrieve()
                .body(new ParameterizedTypeReference<List<Product>>() {
                });
        log.info("received response: {}", products);
        return products;
    }
}

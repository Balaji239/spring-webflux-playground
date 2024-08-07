package com.webfluxplayground.server_sent_events.service;

import com.webfluxplayground.server_sent_events.dto.ProductDto;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class DataSetup implements CommandLineRunner {

    private final ProductService productService;

    public DataSetup(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();
        Flux.range(1, 1000)
                .delayElements(Duration.ofSeconds(1))
                .map(i -> new ProductDto(null, faker.commerce().productName(), ThreadLocalRandom.current().nextInt(1, 100)))
                .flatMap(dto -> productService.saveProduct(Mono.just(dto)))
                .subscribe();
    }
}

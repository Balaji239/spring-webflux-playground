package com.webfluxplayground.r2dbc;

import com.webfluxplayground.r2dbc.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import reactor.test.StepVerifier;

@SpringBootTest
public class ProductRepositoryTest {

    private static final Logger log = LoggerFactory.getLogger(ProductRepositoryTest.class);

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testFindByPriceBetween(){
        productRepository.findByPriceBetween(750,1000)
                .doOnNext(p -> log.info("{}", p))
                .as(StepVerifier::create)
                .expectNextCount(3)
                .expectComplete()
                .verify();
    }

    @Test
    public void testFindByPageable(){
        productRepository.findBy(PageRequest.of(0,3).withSort(Sort.by("price").ascending()))
                .doOnNext(p -> log.info("{}", p))
                .as(StepVerifier::create)
                .assertNext(p -> Assertions.assertEquals(200, p.getPrice()))
                .assertNext(p -> Assertions.assertEquals(250, p.getPrice()))
                .assertNext(p -> Assertions.assertEquals(300, p.getPrice()))
                .expectComplete()
                .verify();
    }
}

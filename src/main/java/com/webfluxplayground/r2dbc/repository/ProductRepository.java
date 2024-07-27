package com.webfluxplayground.r2dbc.repository;

import com.webfluxplayground.r2dbc.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, Integer> {

    Flux<Product> findByPriceBetween(int from, int to);

    Flux<Product> findBy(Pageable pageable);
}

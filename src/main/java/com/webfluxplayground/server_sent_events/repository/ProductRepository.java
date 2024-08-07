package com.webfluxplayground.server_sent_events.repository;

import com.webfluxplayground.server_sent_events.entity.Product;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, Integer> {

}
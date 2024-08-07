package com.webfluxplayground.server_sent_events.service;

import com.webfluxplayground.server_sent_events.dto.ProductDto;
import com.webfluxplayground.server_sent_events.mapper.EntityDtoMapper;
import com.webfluxplayground.server_sent_events.repository.ProductRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final Sinks.Many<ProductDto> productDtoSink;

    public ProductService(ProductRepository productRepository, Sinks.Many<ProductDto> productDtoSink){
        this.productRepository = productRepository;
        this.productDtoSink = productDtoSink;
    }

    public Mono<ProductDto> saveProduct(Mono<ProductDto> productDtoMono){
        return productDtoMono.map(EntityDtoMapper::toEntity)
                .flatMap(productRepository::save)
                .map(EntityDtoMapper::toDto)
                .doOnNext(productDtoSink::tryEmitNext);
    }

    public Flux<ProductDto> productStream(){
        return productDtoSink.asFlux();
    }

}

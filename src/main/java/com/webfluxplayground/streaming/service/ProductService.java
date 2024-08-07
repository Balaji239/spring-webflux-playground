package com.webfluxplayground.streaming.service;

import com.webfluxplayground.streaming.dto.ProductDto;
import com.webfluxplayground.streaming.mapper.EntityDtoMapper;
import com.webfluxplayground.streaming.repository.ProductRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public Flux<ProductDto> saveProducts(Flux<ProductDto> productDtoFlux){
        return productDtoFlux.map(EntityDtoMapper::toEntity)
                .as(productRepository::saveAll)
                .map(EntityDtoMapper::toDto);
    }

    public Mono<Long> getProductsCount(){
        return productRepository.count();
    }

    public Flux<ProductDto> getAllProducts(){
        return productRepository.findAll().map(EntityDtoMapper::toDto);
    }

}

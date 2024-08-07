package com.webfluxplayground.streaming.controller;

import com.webfluxplayground.streaming.dto.ProductDto;
import com.webfluxplayground.streaming.dto.UploadResponse;
import com.webfluxplayground.streaming.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.APPLICATION_NDJSON_VALUE)
    public Mono<UploadResponse> uploadProducts(@RequestBody Flux<ProductDto> productDtoFlux) {
        return productService.saveProducts(productDtoFlux)
                .then(productService.getProductsCount())
                .map(count -> new UploadResponse(UUID.randomUUID(), count));
    }

    @GetMapping(value = "/download", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<ProductDto> downloadProducts() {
        return productService.getAllProducts();
    }
}

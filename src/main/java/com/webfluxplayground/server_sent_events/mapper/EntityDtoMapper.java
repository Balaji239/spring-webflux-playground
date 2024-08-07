package com.webfluxplayground.server_sent_events.mapper;

import com.webfluxplayground.server_sent_events.dto.ProductDto;
import com.webfluxplayground.server_sent_events.entity.Product;

public class EntityDtoMapper {

    public static Product toEntity(ProductDto dto){
        var product = new Product();
        product.setId(dto.id());
        product.setDescription(dto.description());
        product.setPrice(dto.price());
        return product;
    }

    public static ProductDto toDto(Product product){
        return new ProductDto(
                product.getId(),
                product.getDescription(),
                product.getPrice()
        );
    }

}
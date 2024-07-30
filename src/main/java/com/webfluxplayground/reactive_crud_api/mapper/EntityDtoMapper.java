package com.webfluxplayground.reactive_crud_api.mapper;

import com.webfluxplayground.reactive_crud_api.dto.CustomerDto;
import com.webfluxplayground.reactive_crud_api.entity.Customer;

public class EntityDtoMapper {

    public static Customer toEntity(CustomerDto dto) {
        Customer customer = new Customer();
        customer.setId(dto.id());
        customer.setName(dto.name());
        customer.setEmail(dto.email());
        return customer;
    }

    public static CustomerDto toDto(Customer customer) {
        return new CustomerDto(
                customer.getId(),
                customer.getName(),
                customer.getEmail()
        );
    }
}

package com.webfluxplayground.functional_endpoints.mapper;

import com.webfluxplayground.functional_endpoints.dto.CustomerDto;
import com.webfluxplayground.functional_endpoints.entity.Customer;

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

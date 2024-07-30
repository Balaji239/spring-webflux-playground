package com.webfluxplayground.reactive_crud_api.service;

import com.webfluxplayground.reactive_crud_api.dto.CustomerDto;
import com.webfluxplayground.reactive_crud_api.mapper.EntityDtoMapper;
import com.webfluxplayground.reactive_crud_api.repository.CustomerRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Flux<CustomerDto> getAllCustomers() {
        return customerRepository.findAll().map(EntityDtoMapper::toDto);
    }

    public Flux<CustomerDto> getAllCustomers(int pageNo, int size) {
        return customerRepository.findBy(PageRequest.of(pageNo - 1, size))
                .map(EntityDtoMapper::toDto);
    }

    public Mono<CustomerDto> getCustomerById(int id) {
        return customerRepository.findById(id).map(EntityDtoMapper::toDto);
    }

    public Mono<CustomerDto> saveCustomer(Mono<CustomerDto> monoCustomer) {
        return monoCustomer.map(EntityDtoMapper::toEntity)
                .flatMap(customerRepository::save)
                .map(EntityDtoMapper::toDto);
    }

    public Mono<CustomerDto> updateCustomer(int id, CustomerDto customerDto) {
        return customerRepository.findById(id)
                .onErrorComplete()
                .map(customer -> customerDto)
                .map(EntityDtoMapper::toEntity)
                .doOnNext(entity -> {
                    entity.setId(id);
                    customerRepository.save(entity);
                })
                .map(EntityDtoMapper::toDto);
    }

    public Mono<Boolean> deleteCustomerById(int id) {
        return customerRepository.deleteCustomerById(id);
    }

}

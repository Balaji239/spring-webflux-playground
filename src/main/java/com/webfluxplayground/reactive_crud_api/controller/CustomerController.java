package com.webfluxplayground.reactive_crud_api.controller;

import com.webfluxplayground.reactive_crud_api.dto.CustomerDto;
import com.webfluxplayground.reactive_crud_api.exception.ApplicationExceptions;
import com.webfluxplayground.reactive_crud_api.service.CustomerService;
import com.webfluxplayground.reactive_crud_api.validator.RequestValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping()
    public Flux<CustomerDto> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/paginated")
    public Flux<CustomerDto> getAllCustomers(@RequestParam(defaultValue = "1") int pageNo,
                                             @RequestParam(defaultValue = "5") int size) {
        return customerService.getAllCustomers(pageNo, size);
    }

    @GetMapping("/{id}")
    public Mono<CustomerDto> getCustomerById(@PathVariable int id) {
        return customerService.getCustomerById(id)
                .switchIfEmpty(ApplicationExceptions.customerNotFoundMono(id));
    }

    @PostMapping
    public Mono<CustomerDto> saveCustomer(@RequestBody Mono<CustomerDto> mono) {
        return mono.transform(RequestValidator.validate())
                .as(customerService::saveCustomer);
    }

    @PutMapping("/{id}")
    public Mono<CustomerDto> updateCustomer(@PathVariable int id, @RequestBody CustomerDto customerDto) {
        RequestValidator.validate(customerDto);
        return customerService.updateCustomer(id, customerDto)
                .switchIfEmpty(ApplicationExceptions.customerNotFoundMono(id));
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteCustomerById(@PathVariable int id) {
        return customerService.deleteCustomerById(id)
                .filter(b -> b)
                .switchIfEmpty(ApplicationExceptions.customerNotFoundMono(id))
                .then();
    }
}

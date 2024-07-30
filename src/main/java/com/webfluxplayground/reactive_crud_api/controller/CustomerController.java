package com.webfluxplayground.reactive_crud_api.controller;

import com.webfluxplayground.reactive_crud_api.dto.CustomerDto;
import com.webfluxplayground.reactive_crud_api.service.CustomerService;
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
                                             @RequestParam(defaultValue = "5") int size){
        return customerService.getAllCustomers(pageNo, size);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<CustomerDto>> getCustomerById(@PathVariable int id) {
        return customerService.getCustomerById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<CustomerDto>> saveCustomer(@RequestBody Mono<CustomerDto> mono) {
        return customerService.saveCustomer(mono)
                .map(ResponseEntity.status(HttpStatus.CREATED)::body);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<CustomerDto>> updateCustomer(@PathVariable int id, @RequestBody CustomerDto customerDto) {
        return customerService.updateCustomer(id, customerDto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteCustomerById(@PathVariable int id) {
        return customerService.deleteCustomerById(id)
                .filter(b -> b)
                .map(b -> ResponseEntity.status(HttpStatus.NO_CONTENT).<Void>build())
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}

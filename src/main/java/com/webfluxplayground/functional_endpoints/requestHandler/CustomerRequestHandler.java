package com.webfluxplayground.functional_endpoints.requestHandler;

import com.webfluxplayground.functional_endpoints.dto.CustomerDto;
import com.webfluxplayground.functional_endpoints.exception.ApplicationExceptions;
import com.webfluxplayground.functional_endpoints.service.CustomerService;
import com.webfluxplayground.functional_endpoints.validator.RequestValidator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
public class CustomerRequestHandler {

    private final CustomerService customerService;

    public CustomerRequestHandler(CustomerService customerService){
        this.customerService = customerService;
    }

    public Mono<ServerResponse> getAllCustomers(ServerRequest request){
        //return ServerResponse.ok()
        //        .body(customerService.getAllCustomers(), CustomerDto.class);
        // OR
        return customerService.getAllCustomers()
                .as(flux -> ServerResponse.ok().body(flux, CustomerDto.class));
    }

    public Mono<ServerResponse> getAllCustomersPaginated(ServerRequest request){
        int pageNo = request.queryParam("pageNo").map(Integer::parseInt).orElse(1);
        int size = request.queryParam("size").map(Integer::parseInt).orElse(5);
        return customerService.getAllCustomers(pageNo, size)
                .collectList()
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> getCustomerById(ServerRequest request){
        int id = Integer.parseInt(request.pathVariable("id"));
        return customerService.getCustomerById(id)
                .switchIfEmpty(ApplicationExceptions.customerNotFoundMono(id))
                .flatMap(customerMono -> ServerResponse.ok().bodyValue(customerMono));
    }

    public Mono<ServerResponse> saveCustomer(ServerRequest request){
        return request.bodyToMono(CustomerDto.class)
                .transform(RequestValidator.validate())
                .as(customerService::saveCustomer)
                .flatMap(customerMono -> ServerResponse.status(HttpStatus.CREATED).bodyValue(customerMono));
    }

    public Mono<ServerResponse> updateCustomer(ServerRequest request){
        int id = Integer.parseInt(request.pathVariable("id"));
        return request.bodyToMono(CustomerDto.class)
                .transform(RequestValidator.validate())
                .flatMap(customerDto -> customerService.updateCustomer(id, customerDto))
                .switchIfEmpty(ApplicationExceptions.customerNotFoundMono(id))
                .flatMap(customerMono -> ServerResponse.ok().bodyValue(customerMono));
    }

    public Mono<ServerResponse> deleteCustomer(ServerRequest request){
        int id = Integer.parseInt(request.pathVariable("id"));
        return customerService.deleteCustomerById(id)
                .filter(b -> b)
                .switchIfEmpty(ApplicationExceptions.customerNotFoundMono(id))
                .then(ServerResponse.status(HttpStatus.NO_CONTENT).build());
    }
}

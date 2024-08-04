package com.webfluxplayground.functional_endpoints.config;

import com.webfluxplayground.functional_endpoints.exception.ApplicationExceptionHandler;
import com.webfluxplayground.functional_endpoints.exception.CustomerNotFoundException;
import com.webfluxplayground.functional_endpoints.exception.InvalidInputException;
import com.webfluxplayground.functional_endpoints.requestHandler.CustomerRequestHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.Objects;

@Configuration
public class RouterConfig {

    @Autowired
    private CustomerRequestHandler customerRequestHandler;

    @Autowired
    private ApplicationExceptionHandler exceptionHandler;

    /*
     * here, the order of the path is also important.
     * Ex: If /customers/{id} is placed before /customers/paginated and a request is sent for /customers/paginated,
     * then this req would match for /customers/{id}, bcz the router would assume /paginated is a substitution for {id}
     *
     * We can have multiple Router methods like this. We can place all get requests in one router function and
     * remaining in other router function or in any other way.
     */
    @Bean
    public RouterFunction<ServerResponse> customerRoutes() {
        return RouterFunctions.route()
                // based on path, we can route to other router functions to handle the request
                //.path("/customers", () -> customerRoutes2())
                .GET("/customers", customerRequestHandler::getAllCustomers)
                .GET("/customers/paginated", customerRequestHandler::getAllCustomersPaginated)
                .GET("/customers/{id}", customerRequestHandler::getCustomerById)
                .POST("/customers", customerRequestHandler::saveCustomer)
                .PUT("/customers/{id}", customerRequestHandler::updateCustomer)
                .DELETE("/customers/{id}", customerRequestHandler::deleteCustomer)
                .onError(CustomerNotFoundException.class, exceptionHandler::handleException)
                .onError(InvalidInputException.class, exceptionHandler::handleException)
                .onError(RuntimeException.class, exceptionHandler::handleException)
                .build();
    }

    public RouterFunction<ServerResponse> customerRoutes2() {
        return RouterFunctions.route()
                .GET("/customers/paginated", customerRequestHandler::getAllCustomersPaginated)
                .GET("/customers/{id}", customerRequestHandler::getCustomerById)
                .GET("/customers", customerRequestHandler::getAllCustomers)
                .build();
    }

    // ADDING WEB FILTERS
    /*
     * We can place the filter() method anywhere, it will always be executed first before processing the request
     * The order of filters follow the order in which we have placed in the code.
     */
    public RouterFunction<ServerResponse> customerRoutesWithFilters() {
        return RouterFunctions.route()
                .GET("/customers", customerRequestHandler::getAllCustomers)
                .GET("/customers/paginated", customerRequestHandler::getAllCustomersPaginated)
                .GET("/customers/{id}", customerRequestHandler::getCustomerById)
                .POST("/customers", customerRequestHandler::saveCustomer)
                .PUT("/customers/{id}", customerRequestHandler::updateCustomer)
                .DELETE("/customers/{id}", customerRequestHandler::deleteCustomer)
                .onError(CustomerNotFoundException.class, exceptionHandler::handleException)
                .onError(InvalidInputException.class, exceptionHandler::handleException)
                .onError(RuntimeException.class, exceptionHandler::handleException)
                // authentication filter
                .filter(((request, next) -> {
                    // write the filter logic
                    return next.handle(request); // if filter is successful
                    //return ServerResponse.badRequest().build(); // if filter condition is not satisfied
                }))
                // authorization filter
                .filter(((request, next) -> {
                    // write the filter logic
                    return next.handle(request); // if filter is successful
                    //return ServerResponse.badRequest().build(); // if filter condition is not satisfied
                }))
                .build();
    }

    // REQUEST PREDICATES
    public RouterFunction<ServerResponse> customerRoutesWithPredicates() {
        return RouterFunctions.route()
                .GET("/customers",
                        // this request is processed only if the specified condition match along with path matching
                        RequestPredicates.headers(headers -> Objects.nonNull(headers.firstHeader("auth-token"))),
                        customerRequestHandler::getAllCustomers)
                .GET("/customers/paginated", customerRequestHandler::getAllCustomersPaginated)
                .GET("/customers/{id}", customerRequestHandler::getCustomerById)
                .build();
    }
}

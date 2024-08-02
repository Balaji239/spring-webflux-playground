package com.webfluxplayground.reactive_crud_api.filter;

import com.webfluxplayground.reactive_crud_api.constant.Category;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class AuthorizationFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        Category category = exchange.getAttributeOrDefault("category", Category.STANDARD);
        return switch (category){
            case  STANDARD ->  standard(exchange, chain);
            case PRIME -> prime(exchange, chain);
        };
    }

    private Mono<Void> prime(ServerWebExchange exchange, WebFilterChain chain) {
        return chain.filter(exchange);
    }

    private Mono<Void> standard(ServerWebExchange exchange, WebFilterChain chain) {
        boolean isGetRequest = HttpMethod.GET.equals(exchange.getRequest().getMethod());
        if( isGetRequest){
            return chain.filter(exchange);
        }
        return Mono.fromRunnable(() -> exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN));
    }
}

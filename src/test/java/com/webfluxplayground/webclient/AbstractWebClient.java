package com.webfluxplayground.webclient;

import org.springframework.web.reactive.function.client.WebClient;

public abstract class AbstractWebClient {

    protected WebClient getWebClient(){
        return WebClient.builder()
                .baseUrl("http://localhost:7070/demo02")
                .build();
    }
}

package com.webfluxplayground.server_sent_events.dto;

public record ProductDto(Integer id,
                         String description,
                         Integer price) {
}
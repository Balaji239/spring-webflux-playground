package com.webfluxplayground.r2dbc.dto;

import java.time.Instant;
import java.util.UUID;

public record OrderDetails(UUID orderId,
                           String customerName,
                           String productName,
                           int amount,
                           Instant orderDate) {
}

package com.webfluxplayground.r2dbc.entity;

import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.UUID;

public class CustomerOrder {

    @Id
    private UUID orderId;
    private int customerId;
    private int productId;
    private int amount;
    private Instant orderDate;

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Instant getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CustomerOrder{");
        sb.append("orderId=").append(orderId);
        sb.append(", customerId=").append(customerId);
        sb.append(", productId=").append(productId);
        sb.append(", amount=").append(amount);
        sb.append(", orderDate=").append(orderDate);
        sb.append('}');
        return sb.toString();
    }
}

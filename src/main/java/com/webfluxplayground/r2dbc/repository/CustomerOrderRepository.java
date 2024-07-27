package com.webfluxplayground.r2dbc.repository;

import com.webfluxplayground.r2dbc.dto.OrderDetails;
import com.webfluxplayground.r2dbc.entity.CustomerOrder;
import com.webfluxplayground.r2dbc.entity.Product;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface CustomerOrderRepository extends ReactiveCrudRepository<CustomerOrder, UUID> {

    @Query("""
            SELECT
                p.*
            FROM
                customer c
            INNER JOIN customer_order co ON c.id = co.customer_id
            INNER JOIN product p ON co.product_id = p.id
            WHERE
                c.name = :name
            """)
    Flux<Product> getProductsOrderByCustomer(String name);

    @Query("""
            SELECT
                co.order_id,
                c.name AS customer_name,
                p.description AS product_name,
                co.amount,
                co.order_date
            FROM
                customer c
            INNER JOIN customer_order co ON c.id = co.customer_id
            INNER JOIN product p ON co.product_id = p.id
            WHERE
                p.description = :description
            ORDER BY co.amount DESC
            """)
    Flux<OrderDetails> getOrderDetailsByProduct(String description);

}

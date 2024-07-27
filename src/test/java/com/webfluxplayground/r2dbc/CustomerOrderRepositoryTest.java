package com.webfluxplayground.r2dbc;

import com.webfluxplayground.r2dbc.dto.OrderDetails;
import com.webfluxplayground.r2dbc.repository.CustomerOrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.test.StepVerifier;

/*
* for executing complex queries we can use either @Query or we can use DatabaseClient
*/
@SpringBootTest
public class CustomerOrderRepositoryTest {

    private static final Logger log = LoggerFactory.getLogger(CustomerOrderRepositoryTest.class);

    @Autowired
    private DatabaseClient databaseClient;

    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    @Test
    public void testProductsOrderByCustomer() {
        customerOrderRepository.getProductsOrderByCustomer("mike")
                .doOnNext(p -> log.info("{}", p))
                .as(StepVerifier::create)
                .expectNextCount(2)
                .expectComplete()
                .verify();
    }

    @Test
    public void testGetOrderDetailsByProduct() {
        customerOrderRepository.getOrderDetailsByProduct("iphone 20")
                .doOnNext(p -> log.info("{}", p))
                .as(StepVerifier::create)
                .assertNext(dto -> Assertions.assertEquals(975, dto.amount()))
                .assertNext(dto -> Assertions.assertEquals(950, dto.amount()))
                .expectComplete()
                .verify();
    }

    @Test
    public void testGetOrderDetailsByProductUsingDatabaseClient() {
        var query = """
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
                    """;

        databaseClient.sql(query)
                .bind("description", "iphone 20")
                .mapProperties(OrderDetails.class)
                .all()
                .doOnNext(p -> log.info("{}", p))
                .as(StepVerifier::create)
                .assertNext(dto -> Assertions.assertEquals(975, dto.amount()))
                .assertNext(dto -> Assertions.assertEquals(950, dto.amount()))
                .expectComplete()
                .verify();
    }
}

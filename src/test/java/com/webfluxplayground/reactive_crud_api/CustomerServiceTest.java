package com.webfluxplayground.reactive_crud_api;

import com.webfluxplayground.reactive_crud_api.dto.CustomerDto;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureWebTestClient
@SpringBootTest
public class CustomerServiceTest {

    private static final Logger log = LoggerFactory.getLogger(CustomerServiceTest.class);

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void testGetAllCustomers() {
        webTestClient.get().uri("/customers")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(CustomerDto.class)
                .value(list -> log.info("list, {}", list))
                .hasSize(10);
    }

    @Test
    public void testGetAllCustomersPaginated() {
        webTestClient.get().uri("/customers/paginated?pageNo=2&size=3")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .consumeWith(r -> log.info("{}", new String(r.getResponseBody())))
                .jsonPath("$.length()").isEqualTo(3)
                .jsonPath("$[0].id").isEqualTo(4)
                .jsonPath("$[1].id").isEqualTo(5);
    }

    @Test
    public void testGetCustomerById() {
        webTestClient.get().uri("/customers/1")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .consumeWith(r -> log.info("{}", new String(r.getResponseBody())))
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.name").isEqualTo("sam")
                .jsonPath("$.email").isEqualTo("sam@gmail.com");
    }

    @Test
    public void testSaveAndDeleteCustomer() {
        CustomerDto customerDto = new CustomerDto(0, "marshall", "marshall@gmail.com");
        webTestClient.post().uri("/customers")
                .bodyValue(customerDto)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .consumeWith(r -> log.info("{}", new String(r.getResponseBody())))
                .jsonPath("$.id").isEqualTo(11)
                .jsonPath("$.name").isEqualTo("marshall")
                .jsonPath("$.email").isEqualTo("marshall@gmail.com");

        webTestClient.delete().uri("/customers/11")
                .exchange()
                .expectStatus().isNoContent()
                .expectBody().isEmpty();
    }

    @Test
    public void testUpdateCustomer() {
        CustomerDto customerDto = new CustomerDto(0, "noel", "noel@gmail.com");
        webTestClient.put().uri("/customers/10")
                .bodyValue(customerDto)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .consumeWith(r -> log.info("{}", new String(r.getResponseBody())))
                .jsonPath("$.id").isEqualTo(10)
                .jsonPath("$.name").isEqualTo("noel")
                .jsonPath("$.email").isEqualTo("noel@gmail.com");
    }

    @Test
    public void testCustomerNotFound() {
        // get req
        webTestClient.get()
                .uri("/customers/11")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().isEmpty();

        // put req
        CustomerDto customerDto = new CustomerDto(0, "noel", "noel@gmail.com");
        webTestClient.put()
                .uri("/customers/11")
                .bodyValue(customerDto)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().isEmpty();

        // delete req
        webTestClient.delete()
                .uri("/customers/11")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().isEmpty();
    }
}
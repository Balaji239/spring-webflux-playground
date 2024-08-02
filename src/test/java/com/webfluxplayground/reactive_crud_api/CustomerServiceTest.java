package com.webfluxplayground.reactive_crud_api;

import com.webfluxplayground.reactive_crud_api.dto.CustomerDto;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
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
                .expectBody()
                .jsonPath("$.detail").isEqualTo("Customer with [id=11] not found");

        // put req
        CustomerDto customerDto = new CustomerDto(0, "noel", "noel@gmail.com");
        webTestClient.put()
                .uri("/customers/11")
                .bodyValue(customerDto)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.detail").isEqualTo("Customer with [id=11] not found");

        // delete req
        webTestClient.delete()
                .uri("/customers/11")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.detail").isEqualTo("Customer with [id=11] not found");
    }

    @Test
    public void testInvalidInput(){
        CustomerDto missingNameDto = new CustomerDto(0, null,"neol@gmail.com");
        webTestClient.post()
                .uri("/customers")
                .bodyValue(missingNameDto)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.detail").isEqualTo("Name is required");

        CustomerDto missingEmailDto = new CustomerDto(0, "neol",null);
        webTestClient.post()
                .uri("/customers")
                .bodyValue(missingEmailDto)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.detail").isEqualTo("Email is required");

        CustomerDto invalidEmailDto = new CustomerDto(0, "neol","neol.com");
        webTestClient.put()
                .uri("/customers/10")
                .bodyValue(invalidEmailDto)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.detail").isEqualTo("Email is required");
    }

    @Test
    void testRequestWithoutAuthToken(){
        webTestClient.get()
                .uri("/customers")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNAUTHORIZED);

        webTestClient.get()
                .uri("/customers/5")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNAUTHORIZED);

        CustomerDto customerDto = new CustomerDto(0, "neol","neol@gmail.com");
        webTestClient.post()
                .uri("/customers")
                .bodyValue(customerDto)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNAUTHORIZED);

        webTestClient.put()
                .uri("/customers/10")
                .bodyValue(customerDto)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNAUTHORIZED);

        webTestClient.delete()
                .uri("/customers/3")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void testRequestWithInvalidToken(){
        webTestClient.get()
                .uri("/customers")
                .header("auth-token", "abcde")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNAUTHORIZED);

        webTestClient.get()
                .uri("/customers/5")
                .header("auth-token", "12345")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNAUTHORIZED);

        CustomerDto customerDto = new CustomerDto(0, "neol","neol@gmail.com");
        webTestClient.post()
                .uri("/customers")
                .header("auth-token", "secreeet1")
                .bodyValue(customerDto)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNAUTHORIZED);

        webTestClient.put()
                .uri("/customers/10")
                .header("auth-token", "secrete12345")
                .bodyValue(customerDto)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNAUTHORIZED);

        webTestClient.delete()
                .uri("/customers/3")
                .header("auth-token", "token123")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}

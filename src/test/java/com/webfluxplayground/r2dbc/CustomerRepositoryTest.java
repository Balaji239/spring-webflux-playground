package com.webfluxplayground.r2dbc;

import com.webfluxplayground.r2dbc.entity.Customer;
import com.webfluxplayground.r2dbc.repository.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

@SpringBootTest
public class CustomerRepositoryTest {

    private static final Logger log = LoggerFactory.getLogger(CustomerRepositoryTest.class);

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void testFindAll() {
        customerRepository.findAll()
                .doOnNext(c -> log.info("{}", c))
                .as(StepVerifier::create)
                .expectNextCount(10)   // expect 10 items
                .expectComplete()
                .verify();
    }

    @Test
    public void testFindById() {
        customerRepository.findById(25)
                .doOnNext(c -> log.info("{}", c))
                .as(StepVerifier::create)
                .assertNext(c -> Assertions.assertEquals("mike", c.getName()))
                .expectComplete()
                .verify();
    }

    @Test
    public void testFindByName() {
        customerRepository.findByName("jake")
                .doOnNext(c -> log.info("{}", c))
                .as(StepVerifier::create)
                .assertNext(c -> Assertions.assertEquals("jake@gmail.com", c.getEmail()))
                .expectComplete()
                .verify();
    }

    @Test
    public void testFindByEmail() {
        customerRepository.findByEmailEndingWith("ke@gmail.com")
                .doOnNext(c -> log.info("{}", c))
                .as(StepVerifier::create)
                .assertNext(c -> Assertions.assertEquals("mike@gmail.com", c.getEmail()))
                .assertNext(c -> Assertions.assertEquals("jake@gmail.com", c.getEmail()))
                .expectComplete()
                .verify();
    }

    @Test
    public void testInsertAndDeleteCustomer(){
        Customer customer = new Customer();
        customer.setName("john");
        customer.setEmail("john@gmail.com");
        customerRepository.save(customer)
                .doOnNext(c -> log.info("{}",c))
                .as(StepVerifier::create)
                .assertNext(c -> Assertions.assertNotNull(c.getId()))
                .expectComplete()
                .verify();

        // verify count
        customerRepository.count()
                .as(StepVerifier::create)
                .expectNext(11L)
                .expectComplete()
                .verify();

        // verify delete
        customerRepository.deleteById(11)
                .then(customerRepository.count())
                .as(StepVerifier::create)
                .expectNext(10L)
                .expectComplete()
                .verify();
    }

    @Test
    public void testUpdateCustomer() {
        customerRepository.findByName("ethan")
                .doOnNext(c -> c.setName("ethan-new"))
                .flatMap(c -> customerRepository.save(c))
                .doOnNext(c -> log.info("{}", c))
                .as(StepVerifier::create)
                .assertNext(c -> Assertions.assertEquals("ethan-new", c.getName()))
                .expectComplete()
                .verify();
    }

}

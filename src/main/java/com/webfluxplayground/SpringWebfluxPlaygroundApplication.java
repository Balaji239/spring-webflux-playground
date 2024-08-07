package com.webfluxplayground;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@SpringBootApplication(scanBasePackages = "com.webfluxplayground.streaming")
@EnableR2dbcRepositories(basePackages = "com.webfluxplayground.streaming")
public class SpringWebfluxPlaygroundApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringWebfluxPlaygroundApplication.class, args);
	}

}

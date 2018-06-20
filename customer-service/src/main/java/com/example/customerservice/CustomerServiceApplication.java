package com.example.customerservice;

import com.example.customerservice.data.Customer;
import com.example.customerservice.storage.CustomerStorage;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class CustomerServiceApplication {

	@Bean
	ApplicationRunner init(CustomerStorage storage) {
		return args -> storage.deleteAll()
				.thenMany(Flux.just("Alice", "Bob", "Chocolate").map(name -> new Customer(null, name)).flatMap(storage::save))
				.thenMany(storage.findAll())
				.subscribe(System.out::println);
	}

	public static void main(String[] args) {
		SpringApplication.run(CustomerServiceApplication.class, args);
	}
}

package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;


@SpringBootApplication
@PropertySource("classpath:value.properties")
public class AdvertsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdvertsServiceApplication.class, args);
	}

}

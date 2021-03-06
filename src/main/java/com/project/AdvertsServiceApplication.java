package com.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;


@SpringBootApplication
@PropertySource(
		{"classpath:value.properties",
		"classpath:application-value.properties",
		"classpath:mail.properties"}
)
public class AdvertsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdvertsServiceApplication.class, args);
	}

}

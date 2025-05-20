package com.demo.customerapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
public class CustomerManagementApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerManagementApiApplication.class, args);
	}

}

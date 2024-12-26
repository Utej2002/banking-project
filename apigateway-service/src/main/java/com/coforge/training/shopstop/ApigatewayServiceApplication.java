package com.coforge.training.shopstop;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Main class for Gateway Service.
 * Routes requests to other microservices using Spring Cloud Gateway.
 * It also registers itself with Eureka for service discovery.
 */
@EnableDiscoveryClient
@SpringBootApplication
public class ApigatewayServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApigatewayServiceApplication.class, args);
	}

}

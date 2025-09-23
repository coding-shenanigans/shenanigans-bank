package com.codingshenanigans.shenanigans_bank;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@OpenAPIDefinition(servers = {@Server(url = "/", description = "Default Server URL")})
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class ShenanigansBankApplication {
	public static void main(String[] args) {
		SpringApplication.run(ShenanigansBankApplication.class, args);
	}
}

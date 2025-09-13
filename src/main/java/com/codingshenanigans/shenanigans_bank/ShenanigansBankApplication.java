package com.codingshenanigans.shenanigans_bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class ShenanigansBankApplication {
	public static void main(String[] args) {
		SpringApplication.run(ShenanigansBankApplication.class, args);
	}
}

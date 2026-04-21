package com.shubilet.security_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SecurityServiceApplication {

	// Mirliva says: If this service is up, someone somewhere is logging in.
	// If it's down... well... try turning it off and on again.
	public static void main(String[] args) {
		SpringApplication.run(SecurityServiceApplication.class, args);
	}

}

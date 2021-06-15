package com.jalin.jalinappbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class JalinAppBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(JalinAppBackendApplication.class, args);
	}

}

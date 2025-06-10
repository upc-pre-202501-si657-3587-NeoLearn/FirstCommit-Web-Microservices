package com.neolearn.memberships_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MembershipsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MembershipsServiceApplication.class, args);
	}

}

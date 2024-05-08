package com.sunyesle.atddmembership;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class AtddMembershipApplication {

	public static void main(String[] args) {
		SpringApplication.run(AtddMembershipApplication.class, args);
	}

}

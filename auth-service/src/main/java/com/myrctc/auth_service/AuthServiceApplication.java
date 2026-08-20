package com.myrctc.auth_service;

import com.myrctc.auth_service.auth.token.TokenConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(TokenConfig.class)
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class}) //TODO: remove this and implement security config
public class AuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}

}

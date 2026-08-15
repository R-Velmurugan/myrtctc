package com.myrctc.auth_service.auth.token;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "myrctc.auth.token")
public record TokenConfig(long expirationDuration, String secret) {
}

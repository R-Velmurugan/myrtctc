package com.myrctc.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.List;

@AutoConfiguration
public class CommonOpenApiConfig {
    private final String gatewayUrl;

    @PostConstruct
    public void init() {
        System.out.println("Loaded");
    }

    public CommonOpenApiConfig(@Value("${myrctc.gateway.url}") String gatewayUrl) {
        this.gatewayUrl = gatewayUrl;
    }

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                // Registers the Bearer JWT scheme to display the 'Authorize' button in Swagger UI
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                // Routes all Swagger UI requests through the Gateway
                .servers(List.of(
                        new Server().url(gatewayUrl).description("API Gateway")
                ));
    }
}
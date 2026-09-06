package com.myrctc.apigateway.filter;

import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
//This class name should remain exactly same. In Yml, filters is defined as JwtAuth. Springboot later appends GatewayFilterFactory to that config entry
//and looks for that Bean.
public class JwtAuthGatewayFilterFactory extends AbstractGatewayFilterFactory<JwtAuthGatewayFilterFactory.EmptyConfigToSatisfyContract> {
    private final JwtAuthService jwtAuthService;
    public record EmptyConfigToSatisfyContract() {}

    JwtAuthGatewayFilterFactory(JwtAuthService jwtAuthService) {
        super(EmptyConfigToSatisfyContract.class);
        this.jwtAuthService = jwtAuthService;
    }
    @Override
    public GatewayFilter apply(EmptyConfigToSatisfyContract config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if(!request.getHeaders().containsKey("Authorization")) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            final String authHeader = request.getHeaders().getFirst("Authorization");
            if(Objects.isNull(authHeader) || !authHeader.startsWith("Bearer ")) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            final String token = authHeader.substring(7);
            try {
                final Claims claims = jwtAuthService.getClaimsFromToken(token);
                final String userID = claims.getSubject();

                final ServerHttpRequest updatedRequest = request.mutate()
                        .header("userID", userID)
                        .build();

                return chain.filter(exchange.mutate()
                        .request(updatedRequest)
                        .build()
                );
            } catch (final Exception e) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        };
    }
}

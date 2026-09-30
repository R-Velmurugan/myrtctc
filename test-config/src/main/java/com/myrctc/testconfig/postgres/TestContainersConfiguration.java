package com.myrctc.testconfig.postgres;

import com.redis.testcontainers.RedisContainer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;

@TestConfiguration(proxyBeanMethods = false) //disable CGLIB for lite mode
public class TestContainersConfiguration {

    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> postgresqlContainer() {
        return new PostgreSQLContainer<>("postgres:17-alpine")
                .withDatabaseName("myrctc");
    }

    @Bean
    @ServiceConnection
    RedisContainer redisContainer() {
        return new RedisContainer("redis/redis-stack:latest");
    }
}

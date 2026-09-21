package com.myrctc.stationservice;

import com.redis.om.spring.annotations.EnableRedisDocumentRepositories;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "com.myrctc.stationservice.repository")
@EnableRedisDocumentRepositories(basePackages = "com.myrctc.stationservice.redis")
@SpringBootApplication
public class StationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StationServiceApplication.class, args);
    }

}

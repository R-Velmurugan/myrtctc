package com.myrctc.stationservice.redis;

import com.myrctc.stationservice.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component
public class RedisSeeder implements CommandLineRunner {
    private final StationRepository jpaRepository;
    private final StationNameRepository redisRepository;

    @Override
    public void run(String... args) {
        seedData();
    }

    private void seedData() {
        if(redisRepository.count() != 0) return;

        List<StationDocument> documents = jpaRepository.findAll().stream()
                .map(entity -> StationDocument.builder()
                        .stationCode(entity.getStationCode())
                        .stationName(entity.getStationName())
                        .build()
                )
                .toList();

        redisRepository.saveAll(documents);
    }

}

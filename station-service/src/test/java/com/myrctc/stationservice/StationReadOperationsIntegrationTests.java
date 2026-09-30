package com.myrctc.stationservice;

import com.myrctc.stationservice.entity.Station;
import com.myrctc.stationservice.model.StationCode;
import com.myrctc.stationservice.model.dto.StationDto;
import com.myrctc.testconfig.postgres.IntegrationTest;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestClient;
import org.testcontainers.shaded.com.github.dockerjava.core.MediaType;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@IntegrationTest
@DisplayName("Station read operations integration tests")
public class StationReadOperationsIntegrationTests {

    @Autowired
    private RestClient.Builder restClientBuilder;
    @LocalServerPort
    int port;
    @Autowired
    JdbcTemplate jdbcTemplate;

    RestClient restClient;

    @BeforeEach
    void setUp() {
        restClient = restClientBuilder
                .baseUrl(String.format("http://localhost:%d", port))
                .build();
    }

    @DisplayName("Station search scenarios")
    @Nested
    class SearchScenarios {
        @DisplayName("Should return station when correct code is passed")
        @CsvFileSource(resources = "/test-data/station-code-and-name-with-partial-search.csv")
        @ParameterizedTest(name = "{index}. Code {0} should return Station name: {1}")
        void shouldReturnStationWhenCorrectCodeIsPassed(StationCode code, String name) {
            ResponseEntity<StationDto> response = restClient.get()
                    .uri(String.format("/station/search/code?stationCode=%s", code.getStationCode()))
                    .retrieve()
                    .toEntity(StationDto.class);

            assertThat(response.getStatusCode())
                    .isEqualTo(HttpStatus.OK);

            assertThat(response.getBody())
                    .isEqualTo(
                            StationDto.builder()
                                    .stationCode(code)
                                    .stationName(name)
                                    .build()
                    );
        }

        @DisplayName("Should return station when partial name is passed")
        @CsvFileSource(resources = "/test-data/station-code-and-name-with-partial-search.csv")
        @ParameterizedTest(name = "{index}. {2} should return results containing {0} - {1}")
        void shouldReturnStationWhenPartialNameIsPassed(StationCode code, String name, String partialName) {
            ResponseEntity<List<StationDto>> response = restClient.get()
                    .uri(String.format("/station/search/name?stationName=%s", partialName))
                    .retrieve()
                    .toEntity(new ParameterizedTypeReference<>() {});

            assertThat(response.getStatusCode())
                    .isEqualTo(HttpStatus.OK);

            assertThat(response.getBody())
                    .isNotNull()
                    .asInstanceOf(InstanceOfAssertFactories.LIST)
                    .contains(
                            StationDto.builder()
                                    .stationCode(code)
                                    .stationName(name)
                                    .build()
                    );
        }
    }

    @DisplayName("Should delete station when code is passed")
    @Test
    void shouldDeleteStation() {
        ResponseEntity<Boolean> response = restClient.delete()
                .uri("/station?stationCode=TUP")
                .retrieve()
                .toEntity(Boolean.class);

        assertThat(response.getBody())
                .isNotNull()
                .isEqualTo(Boolean.TRUE);

        assertThat(jdbcTemplate.queryForObject("SELECT count(*) FROM station WHERE stationCode=TUP", Integer.class))
                .isEqualTo(0);
    }

    /*@DisplayName("Should create station when appropriate code is passed")
    @Test
    void shouldCreateStation() {

    }*/
}

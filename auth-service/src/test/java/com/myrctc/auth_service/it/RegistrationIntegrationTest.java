package com.myrctc.auth_service.it;

import com.myrctc.auth_service.container_config.PostgresIntegrationTest;
import com.myrctc.auth_service.user.Email;
import com.myrctc.auth_service.user.UserDto;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@PostgresIntegrationTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RegistrationIntegrationTest {
    @Autowired
    private RestClient.Builder restClientBuilder;

    @LocalServerPort
    private int port;
    private RestClient restClient;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        restClient = restClientBuilder.baseUrl(String.format("http://localhost:%d", port)).build();
    }

    @DisplayName("Should create user and save")
    @Order(1)
    @Test
    void shouldCreateUserAndSave() {
        ResponseEntity<String> response = restClient.post()
                .uri("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .body(UserDto.builder()
                        .email(new Email("charles.xavier@marvel.com"))
                        .hashedPassword("magneto")
                        .age(40)
                        .name("Charles")
                        .surname("Xavier")
                        .build()
                )
                .retrieve()
                .toEntity(String.class);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.CREATED);

        assertThat(jdbcTemplate.queryForObject("select count(*) from users", Integer.class))
                .isEqualTo(1);
    }

    @DisplayName("Should not create user in case of same user name")
    @Order(2)
    @Test
    void shouldNotCreateUserInCaseOfSameUserName() {
        ResponseEntity<String> response = restClient.post()
                .uri("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .body(UserDto.builder()
                        .email(new Email("charles.xavier@marvel.com"))
                        .hashedPassword("magneto")
                        .age(40)
                        .name("Charles")
                        .surname("Xavier")
                        .build()
                )
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {})
                .toEntity(String.class);

        assertThat(response).satisfies(res -> {
            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
            assertThat(res.getBody()).isEqualTo("Username is already in use");
        });

        assertThat(jdbcTemplate.queryForObject("select count(*) from users", Integer.class))
                .isEqualTo(1);
    }

    @DisplayName("Should not create user in case of invalid email")
    @Order(3)
    @CsvFileSource(resources = "/test-data/email-validation.csv", numLinesToSkip = 1)
    @ParameterizedTest(name = "{index} Should return HTTP Status {1} for email {0}")
    void shouldNotCreateUserInCaseOfInvalidEmail(String email, int responseCode) {
        final String body = """
            {
              "email" : "%s",
              "hashedPassword" : "password",
              "age" : 40,
              "name" : "John",
              "surname" : "Doe"
            }
            """.formatted(email);
        ResponseEntity<String> response = restClient.post()
                .uri("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {})
                .toEntity(String.class);

        assertThat(response).satisfies(res -> assertThat(res.getStatusCode()).isEqualTo(HttpStatus.valueOf(responseCode)));
    }
}

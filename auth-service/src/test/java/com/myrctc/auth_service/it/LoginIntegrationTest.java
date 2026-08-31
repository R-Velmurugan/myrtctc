package com.myrctc.auth_service.it;

import com.myrctc.auth_service.auth.token.JwtResponse;
import com.myrctc.auth_service.container_config.PostgresIntegrationTest;
import com.myrctc.auth_service.user.Email;
import com.myrctc.auth_service.user.LoginRequest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@PostgresIntegrationTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LoginIntegrationTest {
    @Autowired
    private RestClient.Builder restClientBuilder;

    @LocalServerPort
    private int port;
    private RestClient restClient;

    /*@Autowired
    private JdbcTemplate jdbcTemplate;*/

    @BeforeAll
    void init(){
        restClient = restClientBuilder.baseUrl(String.format("http://localhost:%d", port)).build();

//        Uncomment if running this alone. This user is already created in Registration test
        /*ResponseEntity<String> response = restClient.post()
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
                .isEqualTo(1);*/
    }

    @DisplayName("Should login with proper credentials")
    @Test
    void shouldLoginWithProperCredentials() {
        ResponseEntity<JwtResponse> response = restClient.post()
                .uri("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(LoginRequest.builder()
                        .email(new Email("charles.xavier@marvel.com"))
                        .password("magneto")
                        .build()
                )
                .retrieve()
                .toEntity(JwtResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
                .isNotNull()
                .satisfies(body -> assertThat(body.token().token()).isNotNull());
    }

    @ParameterizedTest(name = "Login should fail for email: {0}, password: {1}")
    @CsvSource({
            "charles.xavier@marvel.com, wrongPassword",
            "meredith.grey@marvel.com, notThePhoenix"
    })
    void shouldFailToLoginForWrongCredentials(String email, String password) {
        ResponseEntity<JwtResponse> response = restClient.post()
                .uri("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(LoginRequest.builder()
                        .email(new Email(email))
                        .password(password)
                        .build()
                )
                .retrieve()
                .onStatus(HttpStatusCode :: isError, (req, res) -> {})
                .toEntity(JwtResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNull();
    }
}

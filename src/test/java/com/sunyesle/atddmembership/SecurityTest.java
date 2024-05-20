package com.sunyesle.atddmembership;

import com.sunyesle.atddmembership.entity.AppUser;
import com.sunyesle.atddmembership.enums.Role;
import com.sunyesle.atddmembership.repository.UserRepository;
import com.sunyesle.atddmembership.security.JwtTokenProvider;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SecurityTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        userRepository.deleteAll();
    }

    @Test
    void JwtAuthenticationFilterSuccess() {
        // given
        String username = "testUser";
        String password = "password";
        AppUser info = userRepository.save(new AppUser(username, password));
        String token = jwtTokenProvider.createToken(info.getId(), info.getRole());

        // when
        ExtractableResponse<Response> response =
                given()
                        .log().all()
                        .basePath("/api/v1/security/test")
                        .contentType(ContentType.JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .when()
                        .get()
                .then()
                        .log().all()
                        .extract();

        // then
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void JwtAuthenticationFilterFailure() {
        // given
        String token = jwtTokenProvider.createToken(-1L, Role.ADMIN);

        // when
        ExtractableResponse<Response> response =
                given()
                        .log().all()
                        .basePath("/api/v1/security/test")
                        .contentType(ContentType.JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .when()
                        .get()
                .then()
                        .log().all()
                        .extract();

        // then
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }
}

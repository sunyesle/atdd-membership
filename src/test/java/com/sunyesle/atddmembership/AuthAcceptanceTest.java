package com.sunyesle.atddmembership;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunyesle.atddmembership.dto.LoginRequest;
import com.sunyesle.atddmembership.dto.TokenResponse;
import com.sunyesle.atddmembership.entity.AppUser;
import com.sunyesle.atddmembership.repository.UserRepository;
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
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthAcceptanceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    ObjectMapper objectMapper;

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        userRepository.deleteAll();
    }

    @Test
    void 로그인을_성공한다() throws JsonProcessingException {
        // given
        String username = "testUser";
        String password = "password";
        AppUser info = userRepository.save(new AppUser(username, encoder.encode(password)));
        LoginRequest loginRequest = new LoginRequest(username, password);

        // when
        ExtractableResponse<Response> response =
                given()
                        .log().all()
                        .basePath("/api/v1/auth/login")
                        .contentType(ContentType.JSON)
                        .body(objectMapper.writeValueAsString(loginRequest))
                .when()
                        .post()
                .then()
                        .log().all()
                        .extract();

        // then
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        TokenResponse token = response.as(TokenResponse.class);
        assertThat(token.getAccessToken()).isNotNull();
    }

    @Test
    void 잘못된_아이디일_경우_로그인을_실패한다() throws JsonProcessingException {
        // given
        String incorrectUsername = "incorrectUser";
        String password = "password";
        LoginRequest loginRequest = new LoginRequest(incorrectUsername, password);

        // when
        ExtractableResponse<Response> response =
                given()
                        .log().all()
                        .basePath("/api/v1/auth/login")
                        .contentType(ContentType.JSON)
                        .body(objectMapper.writeValueAsString(loginRequest))
                .when()
                        .post()
                .then()
                        .log().all()
                        .extract();

        // then
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void 잘못된_비밀번호일_경우_로그인을_실패한다() throws JsonProcessingException {
        // given
        String username = "testUser";
        String password = "password";
        String incorrectPassword = "incorrectPassword";
        AppUser info = userRepository.save(new AppUser(username, encoder.encode(password)));
        LoginRequest loginRequest = new LoginRequest(username, incorrectPassword);

        // when
        ExtractableResponse<Response> response =
                given()
                        .log().all()
                        .basePath("/api/v1/auth/login")
                        .contentType(ContentType.JSON)
                        .body(objectMapper.writeValueAsString(loginRequest))
                .when()
                        .post()
                .then()
                        .log().all()
                        .extract();

        // then
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}

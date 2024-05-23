package com.sunyesle.atddmembership.acceptance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sunyesle.atddmembership.dto.LoginRequest;
import com.sunyesle.atddmembership.dto.TokenResponse;
import com.sunyesle.atddmembership.dto.UserRequest;
import com.sunyesle.atddmembership.dto.UserResponse;
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

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthAcceptanceTest {

    private final static String USERNAME = "username1";
    private final static String PASSWORD = "password1";

    @Autowired
    private UserRepository userRepository;

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;

        userRepository.deleteAll();
    }

    @Test
    void 회원가입을_한다() throws JsonProcessingException {
        // given
        UserRequest request = new UserRequest(USERNAME, PASSWORD);

        // when
        ExtractableResponse<Response> response = 회원가입_요청(request);

        // then
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        UserResponse user = response.as(UserResponse.class);
        assertThat(user.getId()).isNotNull();
        assertThat(user.getUsername()).isEqualTo(USERNAME);
    }

    @Test
    void 로그인을_성공한다() throws JsonProcessingException {
        // given
        회원가입_요청(new UserRequest(USERNAME, PASSWORD));
        LoginRequest loginRequest = new LoginRequest(USERNAME, PASSWORD);

        // when
        ExtractableResponse<Response> response = 로그인_요청(loginRequest);

        // then
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        TokenResponse token = response.as(TokenResponse.class);
        assertThat(token.getAccessToken()).isNotNull();
    }

    @Test
    void 잘못된_아이디일_경우_로그인을_실패한다() throws JsonProcessingException {
        // given
        회원가입_요청(new UserRequest(USERNAME, PASSWORD));
        String incorrectUsername = "incorrectUser1";
        LoginRequest loginRequest = new LoginRequest(incorrectUsername, PASSWORD);

        // when
        ExtractableResponse<Response> response = 로그인_요청(loginRequest);

        // then
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void 잘못된_비밀번호일_경우_로그인을_실패한다() throws JsonProcessingException {
        // given
        String incorrectPassword = "incorrectPassword";
        LoginRequest loginRequest = new LoginRequest(USERNAME, incorrectPassword);

        // when
        ExtractableResponse<Response> response = 로그인_요청(loginRequest);

        // then
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private ExtractableResponse<Response> 회원가입_요청(UserRequest request) {
        Map<String, String> params = new HashMap<>();
        params.put("username", request.getUsername());
        params.put("password", request.getPassword());

        return given()
                .log().all()
                .basePath("/api/v1/auth/signup")
                .contentType(ContentType.JSON)
                .body(params)
            .when()
                .post()
            .then()
                .log().all()
                .extract();
    }

    private ExtractableResponse<Response> 로그인_요청(LoginRequest loginRequest) {
        Map<String, String> params = new HashMap<>();
        params.put("username", loginRequest.getUsername());
        params.put("password", loginRequest.getPassword());

        return given()
                .log().all()
                .basePath("/api/v1/auth/login")
                .contentType(ContentType.JSON)
                .body(params)
            .when()
                .post()
            .then()
                .log().all()
                .extract();
    }
}

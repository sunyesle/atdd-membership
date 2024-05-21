package com.sunyesle.atddmembership.acceptance;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunyesle.atddmembership.dto.*;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserAcceptanceTest {

    @Autowired
    UserRepository userRepository;

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
    void 회원가입을_한다() {
        // given
        String username = "username1";
        String password = "password1";
        UserRequest request = new UserRequest(username, password);

        // when
        ExtractableResponse<Response> response = 회원가입_요청(request);

        // then
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        UserResponse user = response.as(UserResponse.class);
        assertThat(user.getId()).isNotNull();
        assertThat(user.getUsername()).isEqualTo(username);
    }

    @Test
    void 회원_정보를_조회한다(){
        // given
        String username = "username1";
        String password = "password1";
        ExtractableResponse<Response> userSaveResponse = 회원가입_요청(new UserRequest(username, password));
        UserResponse savedUser = userSaveResponse.as(UserResponse.class);
        String token = 로그인_요청(username, password);

        // when
        ExtractableResponse<Response> response =
                given()
                        .log().all()
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .basePath("/api/v1/users")
                        .contentType(ContentType.JSON)
                .when()
                        .get()
                .then()
                        .log().all()
                        .extract();

        // then
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        UserResponse user = response.as(UserResponse.class);
        assertThat(user.getId()).isNotNull();
        assertThat(user.getUsername()).isEqualTo(savedUser.getUsername());
    }

    @Test
    void 회원_정보를_삭제한다(){
        // given
        String username = "username1";
        String password = "password1";
        ExtractableResponse<Response> userSaveResponse = 회원가입_요청(new UserRequest(username, password));
        UserResponse savedUser = userSaveResponse.as(UserResponse.class);
        String token = 로그인_요청(username, password);

        // when
        ExtractableResponse<Response> response =
                given()
                        .log().all()
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .basePath("/api/v1/users")
                        .contentType(ContentType.JSON)
                .when()
                        .delete()
                .then()
                        .log().all()
                        .extract();

        // then
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        assertThat(userRepository.findById(savedUser.getId())).isEmpty();
    }

    private ExtractableResponse<Response> 회원가입_요청(UserRequest request) {
        Map<String, String> params = new HashMap<>();
        params.put("username", request.getUsername());
        params.put("password", request.getPassword());

        return given()
                .log().all()
                .basePath("/api/v1/users")
                .contentType(ContentType.JSON)
                .body(params)
                .when()
                .post()
                .then()
                .log().all()
                .extract();
    }

    private String 로그인_요청(String username, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);

        TokenResponse response =
                given()
                        .log().all()
                        .basePath("/api/v1/auth/login")
                        .contentType(ContentType.JSON)
                        .body(params)
                        .when()
                        .post()
                        .then()
                        .log().all()
                        .statusCode(HttpStatus.OK.value())
                        .extract().as(TokenResponse.class);
        return response.getAccessToken();
    }

}

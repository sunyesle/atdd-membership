package com.sunyesle.atddmembership;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunyesle.atddmembership.dto.TokenResponse;
import com.sunyesle.atddmembership.dto.UserRequest;
import com.sunyesle.atddmembership.dto.UserResponse;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SecurityTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        userRepository.deleteAll();
        userRepository.save(new AppUser("username1", encoder.encode("password1")));
    }

    @Test
    void 인증_정보가_없을_경우_401(){
        // when
        ExtractableResponse<Response> response =
                given()
                        .log().all()
                        .basePath("/api/v1/admin")
                        .contentType(ContentType.JSON)
                .when()
                        .get()
                .then()
                        .log().all()
                        .extract();

        // then
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void 접근_권한이_없을_경우_403(){
        // given
        String token = 로그인_요청("username1", "password1");

        // when
        ExtractableResponse<Response> response =
                given()
                        .log().all()
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .basePath("/api/v1/admin")
                        .contentType(ContentType.JSON)
                .when()
                        .get()
                .then()
                        .log().all()
                        .extract();

        // then
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
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

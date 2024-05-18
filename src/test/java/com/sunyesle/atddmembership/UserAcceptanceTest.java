package com.sunyesle.atddmembership;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.http.HttpStatus;

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
    void 회원가입을_한다() throws JsonProcessingException {
        // given
        String username = "testUser";
        String password = "password";
        UserRequest request = new UserRequest(username, password);

        // when
        ExtractableResponse<Response> response =
                given()
                        .log().all()
                        .basePath("/api/v1/users")
                        .contentType(ContentType.JSON)
                        .body(objectMapper.writeValueAsString(request))
                .when()
                        .post()
                .then()
                        .log().all()
                        .extract();

        // then
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        UserResponse user = response.as(UserResponse.class);
        assertThat(user.getId()).isNotNull();
        assertThat(user.getUsername()).isEqualTo(username);
    }

    @Test
    void 회원_정보를_조회한다(){
        // given
        String username = "testUser";
        String password = "password";
        AppUser info = userRepository.save(new AppUser(username, password));

        // when
        ExtractableResponse<Response> response =
                given()
                        .log().all()
                        .basePath("/api/v1/users/" + info.getId())
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
        assertThat(user.getUsername()).isEqualTo(username);
    }
}

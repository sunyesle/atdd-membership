package com.sunyesle.atddmembership;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MembershipAcceptanceTest {

    @Autowired
    ObjectMapper objectMapper;

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    void 멤버십을_등록한다() throws JsonProcessingException {
        String userId = "testUserId";
        String membershipName = "네이버";
        Integer point = 10000;
        MembershipRequest request = new MembershipRequest(userId, membershipName, point);

        MembershipResponse response =
        given()
                .basePath("/api/v1/memberships")
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post()
        .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(MembershipResponse.class);

        assertThat(response.getId()).isEqualTo(1);
        assertThat(response.getMembershipName()).isEqualTo(membershipName);
    }
}

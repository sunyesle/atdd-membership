package com.sunyesle.atddmembership;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunyesle.atddmembership.dto.MembershipRequest;
import com.sunyesle.atddmembership.dto.MembershipResponse;
import com.sunyesle.atddmembership.repository.MembershipRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static com.sunyesle.atddmembership.constants.MembershipConstants.USER_ID_HEADER;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MembershipAcceptanceTest {

    @Autowired
    MembershipRepository membershipRepository;

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
        // given
        String userId = "testUserId";
        String membershipName = "네이버";
        Integer point = 10000;
        MembershipRequest request = new MembershipRequest(membershipName, point);

        // when
        ExtractableResponse<Response> response =
                given()
                        .log().all()
                        .basePath("/api/v1/memberships")
                        .contentType(ContentType.JSON)
                        .header(USER_ID_HEADER, userId)
                        .body(objectMapper.writeValueAsString(request))
                .when()
                        .post()
                .then()
                        .log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        MembershipResponse membership = response.as(MembershipResponse.class);
        assertThat(membership.getId()).isNotNull();
        assertThat(membership.getMembershipName()).isEqualTo(membershipName);
    }
}

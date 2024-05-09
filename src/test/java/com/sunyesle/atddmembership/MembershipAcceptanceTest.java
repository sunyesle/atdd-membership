package com.sunyesle.atddmembership;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunyesle.atddmembership.dto.MembershipDetailResponse;
import com.sunyesle.atddmembership.dto.MembershipRequest;
import com.sunyesle.atddmembership.dto.MembershipResponse;
import com.sunyesle.atddmembership.entity.Membership;
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

import java.util.List;

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

    @Test
    void 멤버십_목록을_조회한다() {
        // given
        String userId = "testUserId";
        membershipRepository.save(new Membership(userId, "네이버", 10000));
        membershipRepository.save(new Membership(userId, "카카오", 5000));

        // when
        ExtractableResponse<Response> response =
                given()
                        .log().all()
                        .basePath("/api/v1/memberships")
                        .contentType(ContentType.JSON)
                        .header(USER_ID_HEADER, userId)
                .when()
                        .get()
                .then()
                        .log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<MembershipDetailResponse> memberships = response.jsonPath().getList(".", MembershipDetailResponse.class);
        assertThat(memberships).hasSize(2);
    }


    @Test
    void 멤버십을_조회한다() {
        // given
        String userId = "testUserId";
        Long membershipId = 1L;
        membershipRepository.save(new Membership(userId, "네이버", 10000));
        membershipRepository.save(new Membership(userId, "카카오", 5000));

        // when
        ExtractableResponse<Response> response =
                given()
                        .log().all()
                        .basePath("/api/v1/memberships/" + membershipId)
                        .contentType(ContentType.JSON)
                        .header(USER_ID_HEADER, userId)
                .when()
                        .get()
                .then()
                        .log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        MembershipDetailResponse membership = response.as(MembershipDetailResponse.class);
        assertThat(membership.getId()).isEqualTo(membershipId);
        assertThat(membership.getMembershipName()).isEqualTo("네이버");
        assertThat(membership.getPoint()).isEqualTo(10000);
        assertThat(membership.getCreatedAt()).isNotNull();
    }
}

package com.sunyesle.atddmembership;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunyesle.atddmembership.dto.MembershipAccumulateRequest;
import com.sunyesle.atddmembership.dto.MembershipDetailResponse;
import com.sunyesle.atddmembership.dto.MembershipRequest;
import com.sunyesle.atddmembership.dto.MembershipResponse;
import com.sunyesle.atddmembership.entity.Membership;
import com.sunyesle.atddmembership.enums.MembershipType;
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
        membershipRepository.deleteAll();
    }

    @Test
    void 멤버십을_등록한다() throws JsonProcessingException {
        // given
        String userId = "testUserId";
        MembershipType membershipType = MembershipType.NAVER;
        Integer point = 10000;
        MembershipRequest request = new MembershipRequest(membershipType, point);

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
        assertThat(membership.getMembershipType()).isEqualTo(membershipType);
    }

    @Test
    void 멤버십_목록을_조회한다() {
        // given
        String userId = "testUserId";
        membershipRepository.save(Membership.builder().userId(userId).membershipType(MembershipType.NAVER).point(10000).build());
        membershipRepository.save(Membership.builder().userId(userId).membershipType(MembershipType.KAKAO).point(5000).build());

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
        Membership info = membershipRepository.save(Membership.builder().userId(userId).membershipType(MembershipType.NAVER).point(10000).build());
        membershipRepository.save(Membership.builder().userId(userId).membershipType(MembershipType.KAKAO).point(5000).build());

        // when
        ExtractableResponse<Response> response =
                given()
                        .log().all()
                        .basePath("/api/v1/memberships/" + info.getId())
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
        assertThat(membership.getId()).isEqualTo(info.getId());
        assertThat(membership.getMembershipType()).isEqualTo(info.getMembershipType());
        assertThat(membership.getPoint()).isEqualTo(info.getPoint());
        assertThat(membership.getCreatedAt()).isNotNull();
    }

    @Test
    void 멤버십을_삭제한다() {
        // given
        String userId = "testUserId";
        Membership info = membershipRepository.save(Membership.builder().userId(userId).membershipType(MembershipType.NAVER).point(10000).build());

        // when
        ExtractableResponse<Response> response =
                given()
                        .log().all()
                        .basePath("/api/v1/memberships/" + info.getId())
                        .contentType(ContentType.JSON)
                        .header(USER_ID_HEADER, userId)
                .when()
                        .delete()
                .then()
                        .log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        assertThat(membershipRepository.findById(info.getId())).isEmpty();
    }

    @Test
    void 멤버십을_적립한다() throws JsonProcessingException {
        // given
        String userId = "testUserId";
        Membership info = membershipRepository.save(Membership.builder().userId(userId).membershipType(MembershipType.NAVER).point(10000).build());
        MembershipAccumulateRequest request = new MembershipAccumulateRequest(20000);

        // when
        ExtractableResponse<Response> response =
                given()
                        .log().all()
                        .basePath("/api/v1/memberships/" + info.getId() + "/accumulate")
                        .contentType(ContentType.JSON)
                        .header(USER_ID_HEADER, userId)
                        .body(objectMapper.writeValueAsString(request))
                .when()
                        .post()
                .then()
                        .log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        assertThat(membershipRepository.findById(info.getId()).get().getPoint()).isEqualTo(10200);
    }

}

package com.sunyesle.atddmembership;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunyesle.atddmembership.dto.*;
import com.sunyesle.atddmembership.entity.AppUser;
import com.sunyesle.atddmembership.enums.MembershipType;
import com.sunyesle.atddmembership.repository.MembershipRepository;
import com.sunyesle.atddmembership.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MembershipAcceptanceTest {

    private static final String USERNAME = "user1";
    private static final String PASSWORD = "password";

    @Autowired
    MembershipRepository membershipRepository;

    @Autowired
    UserRepository userRepository;

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

        membershipRepository.deleteAll();
        userRepository.deleteAll();
        userRepository.save(new AppUser(USERNAME, encoder.encode(PASSWORD)));
    }

    @Test
    void 멤버십을_등록한다() throws JsonProcessingException {
        // given
        String token = 로그인_요청();
        MembershipType membershipType = MembershipType.NAVER;
        Integer point = 10000;
        MembershipRequest request = new MembershipRequest(membershipType, point);

        // when
        ExtractableResponse<Response> response = 멤버십_등록_요청(token, request);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        MembershipResponse membership = response.as(MembershipResponse.class);
        assertThat(membership.getId()).isNotNull();
        assertThat(membership.getMembershipType()).isEqualTo(membershipType);
    }

    @Test
    void 존재하는_멤버십을_등록할_경우_등록을_실패한다() throws JsonProcessingException {
        // given
        String token = 로그인_요청();
        MembershipType membershipType = MembershipType.NAVER;
        멤버십_등록_요청(token, new MembershipRequest(membershipType, 10000));

        // when
        ExtractableResponse<Response> response = 멤버십_등록_요청(token, new MembershipRequest(membershipType, 5000));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 멤버십_목록을_조회한다() {
        // given
        String token = 로그인_요청();
        멤버십_등록_요청(token, new MembershipRequest(MembershipType.NAVER, 10000));
        멤버십_등록_요청(token, new MembershipRequest(MembershipType.KAKAO, 5000));

        // when
        ExtractableResponse<Response> response =
                given()
                        .log().all()
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .basePath("/api/v1/memberships")
                        .contentType(ContentType.JSON)
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
        String token = 로그인_요청();
        ExtractableResponse<Response> membershipSaveResponse = 멤버십_등록_요청(token, new MembershipRequest(MembershipType.NAVER, 10000));
        MembershipResponse savedMembership = membershipSaveResponse.as(MembershipResponse.class);

        // when
        ExtractableResponse<Response> response =
                given()
                        .log().all()
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .basePath("/api/v1/memberships/" + savedMembership.getId())
                        .contentType(ContentType.JSON)
                .when()
                        .get()
                .then()
                        .log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        MembershipDetailResponse membership = response.as(MembershipDetailResponse.class);
        assertThat(membership.getId()).isEqualTo(savedMembership.getId());
        assertThat(membership.getMembershipType()).isEqualTo(savedMembership.getMembershipType());
        assertThat(membership.getCreatedAt()).isNotNull();
    }

    @Test
    void 멤버십을_삭제한다() {
        // given
        String token = 로그인_요청();
        ExtractableResponse<Response> membershipSaveResponse = 멤버십_등록_요청(token, new MembershipRequest(MembershipType.NAVER, 10000));
        MembershipResponse savedMembership = membershipSaveResponse.as(MembershipResponse.class);

        // when
        ExtractableResponse<Response> response =
                given()
                        .log().all()
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .basePath("/api/v1/memberships/" + savedMembership.getId())
                        .contentType(ContentType.JSON)
                .when()
                        .delete()
                .then()
                        .log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        assertThat(membershipRepository.findById(savedMembership.getId())).isEmpty();
    }

    @Test
    void 멤버십을_적립한다() throws JsonProcessingException {
        // given
        String token = 로그인_요청();
        ExtractableResponse<Response> membershipSaveResponse = 멤버십_등록_요청(token, new MembershipRequest(MembershipType.NAVER, 10000));
        MembershipResponse savedMembership = membershipSaveResponse.as(MembershipResponse.class);
        MembershipAccumulateRequest request = new MembershipAccumulateRequest(20000);

        // when
        ExtractableResponse<Response> response =
                given()
                        .log().all()
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .basePath("/api/v1/memberships/" + savedMembership.getId() + "/accumulate")
                        .contentType(ContentType.JSON)
                        .body(objectMapper.writeValueAsString(request))
                .when()
                        .post()
                .then()
                        .log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        assertThat(membershipRepository.findById(savedMembership.getId()).get().getPoint()).isEqualTo(10200);
    }

    private String 로그인_요청() {
        Map<String, String> params = new HashMap<>();
        params.put("username", USERNAME);
        params.put("password", PASSWORD);

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

    private ExtractableResponse<Response> 멤버십_등록_요청(String token, MembershipRequest request) {
        Map<String, String> params = new HashMap<>();
        params.put("membershipType", request.getMembershipType().name());
        params.put("point", request.getPoint()+"");

        return given()
                .log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .basePath("/api/v1/memberships")
                .contentType(ContentType.JSON)
                .body(params)
            .when()
                .post()
            .then()
                .log().all()
                .extract();
    }
}

package com.sunyesle.atddmembership.study;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;

class RestAssuredTest {

    @Test
    void test() {
        given().baseUri("https://www.naver.com/")
                .when().get()
                .then().log().body()
                .statusCode(HttpStatus.OK.value());
    }
}

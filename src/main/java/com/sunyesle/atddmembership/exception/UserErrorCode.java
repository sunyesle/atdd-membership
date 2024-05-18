package com.sunyesle.atddmembership.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum UserErrorCode implements ErrorCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원정보가 존재하지 않습니다."),
    DUPLICATE_USER(HttpStatus.BAD_REQUEST, "중복된 회원이 존재합니다.");

    private final HttpStatus httpStatus;
    private final String message;

    UserErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}

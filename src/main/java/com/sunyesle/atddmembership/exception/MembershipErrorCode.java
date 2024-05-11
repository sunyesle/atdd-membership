package com.sunyesle.atddmembership.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum MembershipErrorCode {
    MEMBERSHIP_NOT_FOUND(HttpStatus.NOT_FOUND, "멤버쉽이 존재하지 않습니다."),
    NOT_MEMBERSHIP_OWNER(HttpStatus.FORBIDDEN, "멤버십 접근 권한이 없습니다."),
    DUPLICATE_MEMBERSHIP(HttpStatus.BAD_REQUEST, "중복된 멤버십이 존재합니다.");

    private final HttpStatus httpStatus;
    private final String message;

    MembershipErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}

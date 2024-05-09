package com.sunyesle.atddmembership.exception;

import lombok.Getter;

@Getter
public class MembershipException extends RuntimeException {
    private final MembershipErrorCode errorCode;

    public MembershipException(MembershipErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}

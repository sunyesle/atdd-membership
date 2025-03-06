package com.sunyesle.atddmembership.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

public enum MembershipType {
    NAVER, KAKAO, LINE;

    @JsonCreator
    public static MembershipType from(String value) {
        return Arrays.stream(values())
                .filter(type -> type.name().equals(value))
                .findAny()
                .orElse(null);
    }
}

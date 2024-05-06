package com.sunyesle.atddmembership.dto;

import lombok.Getter;

@Getter
public class MembershipRequest {
    private final String userId;
    private final String membershipName;
    private final Integer point;

    public MembershipRequest(String userId, String membershipName, Integer point) {
        this.userId = userId;
        this.membershipName = membershipName;
        this.point = point;
    }
}

package com.sunyesle.atddmembership.dto;

import lombok.Getter;

@Getter
public class MembershipRequest {
    private final String membershipName;
    private final Integer point;

    public MembershipRequest(String membershipName, Integer point) {
        this.membershipName = membershipName;
        this.point = point;
    }
}

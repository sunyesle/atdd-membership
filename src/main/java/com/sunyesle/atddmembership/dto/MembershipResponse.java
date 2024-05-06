package com.sunyesle.atddmembership.dto;

import lombok.Getter;

@Getter
public class MembershipResponse {
    private final Long id;
    private final String membershipName;

    public MembershipResponse(Long id, String membershipName) {
        this.id = id;
        this.membershipName = membershipName;
    }
}

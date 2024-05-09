package com.sunyesle.atddmembership.dto;

import com.sunyesle.atddmembership.enums.MembershipType;
import lombok.Getter;

@Getter
public class MembershipRequest {
    private final MembershipType membershipType;
    private final Integer point;

    public MembershipRequest(MembershipType membershipType, Integer point) {
        this.membershipType = membershipType;
        this.point = point;
    }
}

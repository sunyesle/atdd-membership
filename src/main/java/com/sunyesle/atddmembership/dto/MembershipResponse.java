package com.sunyesle.atddmembership.dto;

import com.sunyesle.atddmembership.enums.MembershipType;
import lombok.Getter;

@Getter
public class MembershipResponse {
    private final Long id;
    private final MembershipType membershipType;

    public MembershipResponse(Long id, MembershipType membershipType) {
        this.id = id;
        this.membershipType = membershipType;
    }
}

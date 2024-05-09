package com.sunyesle.atddmembership.dto;

import com.sunyesle.atddmembership.entity.Membership;
import com.sunyesle.atddmembership.enums.MembershipType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MembershipDetailResponse {
    private final Long id;
    private final MembershipType membershipType;
    private final Integer point;
    private final LocalDateTime createdAt;

    public MembershipDetailResponse(Long id, MembershipType membershipType, Integer point, LocalDateTime createdAt) {
        this.id = id;
        this.membershipType = membershipType;
        this.point = point;
        this.createdAt = createdAt;
    }

    public static MembershipDetailResponse of(Membership membership){
        return new MembershipDetailResponse(membership.getId(), membership.getMembershipType(), membership.getPoint(), membership.getCreatedAt());
    }
}

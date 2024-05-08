package com.sunyesle.atddmembership.dto;

import com.sunyesle.atddmembership.entity.Membership;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MembershipDetailResponse {
    private final Long id;
    private final String membershipName;
    private final Integer point;
    private final LocalDateTime createdAt;

    public MembershipDetailResponse(Long id, String membershipName, Integer point, LocalDateTime createdAt) {
        this.id = id;
        this.membershipName = membershipName;
        this.point = point;
        this.createdAt = createdAt;
    }

    public static MembershipDetailResponse of(Membership membership){
        return new MembershipDetailResponse(membership.getId(), membership.getMembershipName(), membership.getPoint(), membership.getCreatedAt());
    }
}

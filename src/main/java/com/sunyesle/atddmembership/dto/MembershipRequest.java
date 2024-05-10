package com.sunyesle.atddmembership.dto;

import com.sunyesle.atddmembership.enums.MembershipType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MembershipRequest {
    @NotNull
    private final MembershipType membershipType;

    @NotNull
    @Min(0)
    private final Integer point;

    public MembershipRequest(MembershipType membershipType, Integer point) {
        this.membershipType = membershipType;
        this.point = point;
    }
}

package com.sunyesle.atddmembership.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class MembershipAccumulateRequest {
    private final Integer amount;

    @JsonCreator
    public MembershipAccumulateRequest(Integer amount) {
        this.amount = amount;
    }
}

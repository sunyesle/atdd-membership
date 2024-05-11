package com.sunyesle.atddmembership.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MembershipAccumulateRequest {
    @NotNull
    @Min(0)
    private final Integer amount;

    @JsonCreator
    public MembershipAccumulateRequest(Integer amount) {
        this.amount = amount;
    }
}

package com.sunyesle.atddmembership.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class TokenResponse {
    private final String accessToken;

    @JsonCreator
    public TokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}

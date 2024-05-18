package com.sunyesle.atddmembership.dto;

import com.sunyesle.atddmembership.entity.AppUser;
import lombok.Getter;

@Getter
public class UserResponse {
    private final Long id;
    private final String username;

    public UserResponse(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public static UserResponse of(AppUser user) {
        return new UserResponse(user.getId(), user.getUsername());
    }
}

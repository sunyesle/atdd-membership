package com.sunyesle.atddmembership.dto;

import com.sunyesle.atddmembership.entity.AppUser;
import lombok.Getter;

@Getter
public class UserRequest {
    private final String username;
    private final String password;

    public UserRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public AppUser toUser(){
        return new AppUser(username, password);
    }

}

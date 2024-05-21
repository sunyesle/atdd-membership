package com.sunyesle.atddmembership.dto;

import com.sunyesle.atddmembership.entity.AppUser;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UserRequest {
    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9]{3,20}$", message = "아이디는 3~20자 영문, 숫자 조합이어야합니다.")
    private final String username;

    @NotNull
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z]).{8,20}", message = "비밀번호는 8~20자 영문, 숫자 조합이어야합니다.")
    private final String password;


    public UserRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public AppUser toUser(){
        return new AppUser(username, password);
    }

}

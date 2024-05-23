package com.sunyesle.atddmembership.controller;

import com.sunyesle.atddmembership.dto.UserResponse;
import com.sunyesle.atddmembership.security.CustomUserDetails;
import com.sunyesle.atddmembership.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserResponse> getUser(@AuthenticationPrincipal CustomUserDetails user) {
        UserResponse response = userService.getUser(user.getId());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping
    public ResponseEntity<UserResponse> deleteUser(@AuthenticationPrincipal CustomUserDetails user) {
        userService.deleteUser(user.getId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

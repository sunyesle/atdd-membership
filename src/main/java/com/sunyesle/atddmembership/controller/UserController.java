package com.sunyesle.atddmembership.controller;

import com.sunyesle.atddmembership.dto.UserRequest;
import com.sunyesle.atddmembership.dto.UserResponse;
import com.sunyesle.atddmembership.security.CustomUserDetails;
import com.sunyesle.atddmembership.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest request) {
        UserResponse response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

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

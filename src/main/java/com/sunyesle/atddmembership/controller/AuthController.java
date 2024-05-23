package com.sunyesle.atddmembership.controller;

import com.sunyesle.atddmembership.dto.LoginRequest;
import com.sunyesle.atddmembership.dto.TokenResponse;
import com.sunyesle.atddmembership.dto.UserRequest;
import com.sunyesle.atddmembership.dto.UserResponse;
import com.sunyesle.atddmembership.service.AuthService;
import com.sunyesle.atddmembership.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(@Valid @RequestBody UserRequest request) {
        UserResponse response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        TokenResponse response = authService.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}

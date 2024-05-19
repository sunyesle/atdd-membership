package com.sunyesle.atddmembership.service;

import com.sunyesle.atddmembership.dto.LoginRequest;
import com.sunyesle.atddmembership.dto.TokenResponse;
import com.sunyesle.atddmembership.entity.AppUser;
import com.sunyesle.atddmembership.exception.CustomException;
import com.sunyesle.atddmembership.exception.UserErrorCode;
import com.sunyesle.atddmembership.repository.UserRepository;
import com.sunyesle.atddmembership.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenResponse login(LoginRequest request) {
        AppUser user = userRepository.findByUsername(request.getUsername()).orElseThrow(()-> new CustomException(UserErrorCode.USER_LOGIN_FAILED));
        if(!encoder.matches(request.getPassword(), user.getPassword())){
            throw new CustomException(UserErrorCode.USER_LOGIN_FAILED);
        }
        String token = jwtTokenProvider.createToken(user.getId(), user.getRole());
        return new TokenResponse(token);
    }
}

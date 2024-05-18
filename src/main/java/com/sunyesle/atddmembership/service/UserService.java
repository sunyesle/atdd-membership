package com.sunyesle.atddmembership.service;

import com.sunyesle.atddmembership.dto.UserRequest;
import com.sunyesle.atddmembership.dto.UserResponse;
import com.sunyesle.atddmembership.entity.AppUser;
import com.sunyesle.atddmembership.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    public UserResponse createUser(UserRequest request) {
        if(userRepository.existsByUsername(request.getUsername())){
            throw new RuntimeException("중복된 아이디가 존재합니다.");
        }
        AppUser user = new AppUser(request.getUsername(), encoder.encode(request.getPassword()));
        userRepository.save(user);
        return UserResponse.of(user);
    }

    public UserResponse getUser(Long id) {
        AppUser user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));
        return UserResponse.of(user);
    }
}

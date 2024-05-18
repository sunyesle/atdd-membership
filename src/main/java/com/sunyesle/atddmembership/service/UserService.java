package com.sunyesle.atddmembership.service;

import com.sunyesle.atddmembership.dto.UserRequest;
import com.sunyesle.atddmembership.dto.UserResponse;
import com.sunyesle.atddmembership.entity.AppUser;
import com.sunyesle.atddmembership.exception.CustomException;
import com.sunyesle.atddmembership.exception.UserErrorCode;
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
            throw new CustomException(UserErrorCode.DUPLICATE_USER);
        }
        AppUser user = new AppUser(request.getUsername(), encoder.encode(request.getPassword()));
        userRepository.save(user);
        return UserResponse.of(user);
    }

    public UserResponse getUser(Long id) {
        AppUser user = userRepository.findById(id).orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
        return UserResponse.of(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}

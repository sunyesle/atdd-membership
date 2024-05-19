package com.sunyesle.atddmembership.security;

import com.sunyesle.atddmembership.entity.AppUser;
import com.sunyesle.atddmembership.exception.CustomException;
import com.sunyesle.atddmembership.exception.UserErrorCode;
import com.sunyesle.atddmembership.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        AppUser user = userRepository.findById(Long.parseLong(id)).orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
        return new CustomUserDetails(user);
    }
}

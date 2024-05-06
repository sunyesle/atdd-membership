package com.sunyesle.atddmembership.service;

import com.sunyesle.atddmembership.dto.MembershipRequest;
import com.sunyesle.atddmembership.dto.MembershipResponse;
import com.sunyesle.atddmembership.repository.MembershipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MembershipService {
    private final MembershipRepository membershipRepository;

    @Transactional
    public MembershipResponse createMembership(MembershipRequest request) {
        return null;
    }
}

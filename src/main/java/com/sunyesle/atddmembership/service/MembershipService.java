package com.sunyesle.atddmembership.service;

import com.sunyesle.atddmembership.dto.MembershipRequest;
import com.sunyesle.atddmembership.dto.MembershipResponse;
import com.sunyesle.atddmembership.entity.Membership;
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
    public MembershipResponse createMembership(String userId, MembershipRequest request) {
        Membership membership = membershipRepository.save(new Membership(userId, request.getMembershipName(), request.getPoint()));
        return new MembershipResponse(membership.getId(), membership.getMembershipName());
    }
}

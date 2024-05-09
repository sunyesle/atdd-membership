package com.sunyesle.atddmembership.service;

import com.sunyesle.atddmembership.dto.MembershipDetailResponse;
import com.sunyesle.atddmembership.dto.MembershipRequest;
import com.sunyesle.atddmembership.dto.MembershipResponse;
import com.sunyesle.atddmembership.entity.Membership;
import com.sunyesle.atddmembership.exception.MembershipErrorCode;
import com.sunyesle.atddmembership.exception.MembershipException;
import com.sunyesle.atddmembership.repository.MembershipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MembershipService {
    private final MembershipRepository membershipRepository;

    @Transactional
    public MembershipResponse createMembership(String userId, MembershipRequest request) {
        Membership membership = membershipRepository.save(Membership.builder().userId(userId).membershipName(request.getMembershipName()).point(request.getPoint()).build());
        return new MembershipResponse(membership.getId(), membership.getMembershipName());
    }

    public List<MembershipDetailResponse> getMemberships(String userId) {
        List<Membership> memberships = membershipRepository.findAllByUserId(userId);
        return memberships.stream().map(MembershipDetailResponse::of).collect(Collectors.toList());
    }

    public MembershipDetailResponse getMembership(String userId, Long id) {
        Membership membership = membershipRepository.findById(id).orElseThrow(() -> new MembershipException(MembershipErrorCode.MEMBERSHIP_NOT_FOUND));
        if(!userId.equals(membership.getUserId())){
            throw new MembershipException(MembershipErrorCode.NOT_MEMBERSHIP_OWNER);
        }
        return MembershipDetailResponse.of(membership);
    }
}

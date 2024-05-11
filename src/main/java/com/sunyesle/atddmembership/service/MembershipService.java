package com.sunyesle.atddmembership.service;

import com.sunyesle.atddmembership.dto.MembershipAccumulateRequest;
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
    private final PointCalculator pointCalculator;

    @Transactional
    public MembershipResponse createMembership(String userId, MembershipRequest request) {
        boolean exists = membershipRepository.existsByUserIdAndMembershipType(userId, request.getMembershipType());
        if(exists){
            throw new MembershipException(MembershipErrorCode.DUPLICATE_MEMBERSHIP);
        }
        Membership savedMembership = membershipRepository.save(Membership.builder().userId(userId).membershipType(request.getMembershipType()).point(request.getPoint()).build());
        return new MembershipResponse(savedMembership.getId(), savedMembership.getMembershipType());
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

    @Transactional
    public void deleteMembership(String userId, Long id) {
        Membership membership = membershipRepository.findById(id).orElseThrow(() -> new MembershipException(MembershipErrorCode.MEMBERSHIP_NOT_FOUND));
        if(!userId.equals(membership.getUserId())){
            throw new MembershipException(MembershipErrorCode.NOT_MEMBERSHIP_OWNER);
        }
        membershipRepository.deleteById(id);
    }

    @Transactional
    public void accumulateMembership(String userId, Long id, MembershipAccumulateRequest request) {
        Membership membership = membershipRepository.findById(id).orElseThrow(() -> new MembershipException(MembershipErrorCode.MEMBERSHIP_NOT_FOUND));
        if (!userId.equals(membership.getUserId())) {
            throw new MembershipException(MembershipErrorCode.NOT_MEMBERSHIP_OWNER);
        }
        membership.addPoint(pointCalculator.calculatePoint(request.getAmount()));
    }
}

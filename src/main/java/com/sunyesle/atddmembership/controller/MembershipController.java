package com.sunyesle.atddmembership.controller;

import com.sunyesle.atddmembership.dto.MembershipAccumulateRequest;
import com.sunyesle.atddmembership.dto.MembershipDetailResponse;
import com.sunyesle.atddmembership.dto.MembershipRequest;
import com.sunyesle.atddmembership.dto.MembershipResponse;
import com.sunyesle.atddmembership.security.CustomUserDetails;
import com.sunyesle.atddmembership.service.MembershipService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/memberships")
public class MembershipController {
    private final MembershipService membershipService;

    @PostMapping
    public ResponseEntity<MembershipResponse> createMembership(@AuthenticationPrincipal CustomUserDetails user, @Valid @RequestBody MembershipRequest request) {
        MembershipResponse response = membershipService.createMembership(user.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<MembershipDetailResponse>> getMemberships(@AuthenticationPrincipal CustomUserDetails user) {
        List<MembershipDetailResponse> response = membershipService.getMemberships(user.getId());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MembershipDetailResponse> getMembership(@AuthenticationPrincipal CustomUserDetails user, @PathVariable Long id) {
        MembershipDetailResponse response = membershipService.getMembership(user.getId(), id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMembership(@AuthenticationPrincipal CustomUserDetails user, @PathVariable Long id) {
        membershipService.deleteMembership(user.getId(), id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/{id}/accumulate")
    public ResponseEntity<Void> accumulateMembership(@AuthenticationPrincipal CustomUserDetails user, @PathVariable Long id, @Valid @RequestBody MembershipAccumulateRequest request) {
        membershipService.accumulateMembership(user.getId(), id, request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

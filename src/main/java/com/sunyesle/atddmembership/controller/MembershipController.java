package com.sunyesle.atddmembership.controller;

import com.sunyesle.atddmembership.dto.MembershipRequest;
import com.sunyesle.atddmembership.dto.MembershipResponse;
import com.sunyesle.atddmembership.service.MembershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/memberships")
public class MembershipController {
    private final MembershipService membershipService;

    @PostMapping
    public ResponseEntity<MembershipResponse> createMembership(@RequestBody MembershipRequest request) {
        MembershipResponse response = membershipService.createMembership(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

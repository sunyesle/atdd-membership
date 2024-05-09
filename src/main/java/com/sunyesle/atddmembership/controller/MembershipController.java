package com.sunyesle.atddmembership.controller;

import com.sunyesle.atddmembership.dto.MembershipDetailResponse;
import com.sunyesle.atddmembership.dto.MembershipRequest;
import com.sunyesle.atddmembership.dto.MembershipResponse;
import com.sunyesle.atddmembership.service.MembershipService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sunyesle.atddmembership.constants.MembershipConstants.USER_ID_HEADER;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/memberships")
public class MembershipController {
    private final MembershipService membershipService;

    @PostMapping
    public ResponseEntity<MembershipResponse> createMembership(@RequestHeader(USER_ID_HEADER) String userId, @RequestBody MembershipRequest request) {
        MembershipResponse response = membershipService.createMembership(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<MembershipDetailResponse>> createMembership(@RequestHeader(USER_ID_HEADER) String userId) {
        List<MembershipDetailResponse> response = membershipService.getMemberships(userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MembershipDetailResponse> createMembership(@RequestHeader(USER_ID_HEADER) String userId, @PathVariable Long id) {
        MembershipDetailResponse response = membershipService.getMembership(userId, id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMembership(@RequestHeader(USER_ID_HEADER) String userId, @PathVariable Long id) {
        membershipService.deleteMembership(userId, id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

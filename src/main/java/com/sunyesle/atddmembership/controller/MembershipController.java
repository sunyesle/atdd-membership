package com.sunyesle.atddmembership.controller;

import com.sunyesle.atddmembership.dto.MembershipRequest;
import com.sunyesle.atddmembership.dto.MembershipResponse;
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

    @PostMapping
    public ResponseEntity<MembershipResponse> createMembership(@RequestBody MembershipRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new MembershipResponse(1L, "네이버"));
    }
}

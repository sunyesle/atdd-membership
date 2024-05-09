package com.sunyesle.atddmembership.repository;

import com.sunyesle.atddmembership.entity.Membership;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MembershipRepository extends JpaRepository<Membership, Long> {
    List<Membership> findAllByUserId(String userId);
}
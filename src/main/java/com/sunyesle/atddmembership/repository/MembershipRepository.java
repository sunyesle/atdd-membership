package com.sunyesle.atddmembership.repository;

import com.sunyesle.atddmembership.entity.Membership;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipRepository extends JpaRepository<Membership, Long> {
}

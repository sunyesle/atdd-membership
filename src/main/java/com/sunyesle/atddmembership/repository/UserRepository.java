package com.sunyesle.atddmembership.repository;

import com.sunyesle.atddmembership.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    boolean existsByUsername(String username);
}

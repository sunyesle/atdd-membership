package com.sunyesle.atddmembership.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Membership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    private String membershipName;

    private Integer point;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public Membership(String userId, String membershipName, Integer point) {
        this.userId = userId;
        this.membershipName = membershipName;
        this.point = point;
    }

    public Membership() {
    }

    public Membership(Long id, String userId, String membershipName, Integer point, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.membershipName = membershipName;
        this.point = point;
        this.createdAt = createdAt;
    }
}

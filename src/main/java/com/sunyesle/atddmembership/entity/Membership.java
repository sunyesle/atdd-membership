package com.sunyesle.atddmembership.entity;

import com.sunyesle.atddmembership.enums.MembershipType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@ToString
public class Membership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    @Enumerated(EnumType.STRING)
    private MembershipType membershipType;

    private Integer point;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public Membership() {
    }

    @Builder
    public Membership(Long id, String userId, MembershipType membershipType, Integer point, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.membershipType = membershipType;
        this.point = point;
        this.createdAt = createdAt;
    }
}

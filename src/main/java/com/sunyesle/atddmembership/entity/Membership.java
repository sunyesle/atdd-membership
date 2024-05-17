package com.sunyesle.atddmembership.entity;

import com.sunyesle.atddmembership.enums.MembershipType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@ToString
public class Membership extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    @Enumerated(EnumType.STRING)
    private MembershipType membershipType;

    private Integer point;

    public Membership() {
    }

    @Builder
    public Membership(Long id, String userId, MembershipType membershipType, Integer point) {
        this.id = id;
        this.userId = userId;
        this.membershipType = membershipType;
        this.point = point;
    }

    public void addPoint(int point) {
        this.point += point;
    }
}

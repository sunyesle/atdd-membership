package com.sunyesle.atddmembership.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Membership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    private String membershipName;

    private Integer point;

    public Membership(String userId, String membershipName, Integer point) {
        this.userId = userId;
        this.membershipName = membershipName;
        this.point = point;
    }
}

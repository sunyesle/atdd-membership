package com.sunyesle.atddmembership.entity;

import com.sunyesle.atddmembership.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class AppUser extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    public AppUser() {
    }

    public AppUser(String username, String password) {
        this.username = username;
        this.password = password;
        this.role = Role.USER;
    }
}

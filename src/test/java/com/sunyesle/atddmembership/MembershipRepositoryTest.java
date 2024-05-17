package com.sunyesle.atddmembership;

import com.sunyesle.atddmembership.config.JpaAuditingConfig;
import com.sunyesle.atddmembership.entity.Membership;
import com.sunyesle.atddmembership.enums.MembershipType;
import com.sunyesle.atddmembership.repository.MembershipRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaAuditingConfig.class)
class MembershipRepositoryTest {

    @Autowired
    MembershipRepository membershipRepository;

    @BeforeEach
    public void setUp() {
        membershipRepository.deleteAll();
    }

    @Test
    void save(){
        Membership membership = new Membership(null, "testUser", MembershipType.NAVER, 10000);

        membershipRepository.save(membership);

        assertThat(membership.getId()).isNotNull();
        assertThat(membership.getCreatedAt()).isNotNull();
        assertThat(membership.getModifiedAt()).isNotNull();
    }
}

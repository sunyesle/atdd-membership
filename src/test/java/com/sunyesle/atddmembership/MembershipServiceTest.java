package com.sunyesle.atddmembership;

import com.sunyesle.atddmembership.dto.MembershipDetailResponse;
import com.sunyesle.atddmembership.entity.Membership;
import com.sunyesle.atddmembership.repository.MembershipRepository;
import com.sunyesle.atddmembership.service.MembershipService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MembershipServiceTest {
    @Mock
    private MembershipRepository repository;

    private MembershipService membershipService;


    @BeforeEach
    void setUp() {
        membershipService = new MembershipService(repository);
    }

    @Test
    void 멤버십_조회를_성공한다(){
        // given
        String userId = "testUser";
        Long membershipId = 1L;
        given(repository.findById(membershipId))
                .willReturn(Optional.of(new Membership(membershipId, userId, "네이버", 10000, LocalDateTime.now())));

        // when
        MembershipDetailResponse response = membershipService.getMembership(userId, membershipId);

        // then
        assertThat(response.getId()).isEqualTo(membershipId);
        assertThat(response.getCreatedAt()).isNotNull();
    }
}

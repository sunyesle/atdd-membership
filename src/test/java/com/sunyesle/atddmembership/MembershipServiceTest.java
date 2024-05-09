package com.sunyesle.atddmembership;

import com.sunyesle.atddmembership.dto.MembershipDetailResponse;
import com.sunyesle.atddmembership.entity.Membership;
import com.sunyesle.atddmembership.exception.MembershipErrorCode;
import com.sunyesle.atddmembership.exception.MembershipException;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    @Test
    void 존재하지_않는_멤버십을_조회할_경우_예외가_발생한다(){
        // given
        String userId = "testUser";
        Long membershipId = 1L;
        given(repository.findById(membershipId))
                .willReturn(Optional.empty());

        // when then
        assertThatThrownBy(() -> { membershipService.getMembership(userId, membershipId); })
                .isInstanceOf(MembershipException.class)
                .hasMessageContaining(MembershipErrorCode.MEMBERSHIP_NOT_FOUND.getMessage());
    }

    @Test
    void 권한이_없는_멤버십을_조회할_경우_예외가_발생한다(){
        // given
        String userId = "testUser";
        Long membershipId = 1L;
        given(repository.findById(membershipId))
                .willReturn(Optional.of(new Membership(membershipId, "anotherUser", "네이버", 10000, LocalDateTime.now())));

        // when then
        assertThatThrownBy(() -> { membershipService.getMembership(userId, membershipId); })
                .isInstanceOf(MembershipException.class)
                .hasMessageContaining(MembershipErrorCode.NOT_MEMBERSHIP_OWNER.getMessage());
    }
}

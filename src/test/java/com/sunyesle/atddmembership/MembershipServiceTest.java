package com.sunyesle.atddmembership;

import com.sunyesle.atddmembership.dto.MembershipDetailResponse;
import com.sunyesle.atddmembership.dto.MembershipRequest;
import com.sunyesle.atddmembership.dto.MembershipResponse;
import com.sunyesle.atddmembership.entity.Membership;
import com.sunyesle.atddmembership.enums.MembershipType;
import com.sunyesle.atddmembership.exception.MembershipErrorCode;
import com.sunyesle.atddmembership.exception.MembershipException;
import com.sunyesle.atddmembership.repository.MembershipRepository;
import com.sunyesle.atddmembership.service.MembershipService;
import com.sunyesle.atddmembership.service.PointCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MembershipServiceTest {
    @Mock
    private MembershipRepository repository;

    @Mock
    private PointCalculator pointCalculator;

    private MembershipService membershipService;

    @BeforeEach
    void setUp() {
        membershipService = new MembershipService(repository, pointCalculator);
    }

    @Test
    void 멤버십_등록을_성공한다(){
        // given
        String userId = "testUser";
        MembershipType membershipType = MembershipType.NAVER;
        Integer point = 10000;
        MembershipRequest request = new MembershipRequest(membershipType, point);
        given(repository.existsByUserIdAndMembershipType(userId, membershipType))
                .willReturn(false);
        given(repository.save(any()))
                .willReturn(new Membership(1L, userId, membershipType, point));

        // when
        MembershipResponse response = membershipService.createMembership(userId, request);

        // then
        assertThat(response.getMembershipType()).isEqualTo(membershipType);
    }

    @Test
    void 멤버십을_중복으로_등록할_경우_예외가_발생한다(){
        // given
        String userId = "testUser";
        MembershipType membershipType = MembershipType.NAVER;
        MembershipRequest request = new MembershipRequest(membershipType, 5000);
        given(repository.existsByUserIdAndMembershipType(userId, membershipType))
                .willReturn(true);

        // when then
        assertThatThrownBy(() -> { membershipService.createMembership(userId, request); })
                .isInstanceOf(MembershipException.class)
                .hasMessageContaining(MembershipErrorCode.DUPLICATE_MEMBERSHIP.getMessage());
    }

    @Test
    void 멤버십_조회를_성공한다(){
        // given
        String userId = "testUser";
        Long membershipId = 1L;
        given(repository.findById(membershipId))
                .willReturn(Optional.of(new Membership(membershipId, userId, MembershipType.NAVER, 10000)));

        // when
        MembershipDetailResponse response = membershipService.getMembership(userId, membershipId);

        // then
        assertThat(response.getId()).isEqualTo(membershipId);
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
                .willReturn(Optional.of(new Membership(membershipId, "anotherUser", MembershipType.NAVER, 10000)));

        // when then
        assertThatThrownBy(() -> { membershipService.getMembership(userId, membershipId); })
                .isInstanceOf(MembershipException.class)
                .hasMessageContaining(MembershipErrorCode.NOT_MEMBERSHIP_OWNER.getMessage());
    }
}

package com.sunyesle.atddmembership;

import com.sunyesle.atddmembership.dto.MembershipDetailResponse;
import com.sunyesle.atddmembership.dto.MembershipRequest;
import com.sunyesle.atddmembership.dto.MembershipResponse;
import com.sunyesle.atddmembership.entity.Membership;
import com.sunyesle.atddmembership.enums.MembershipType;
import com.sunyesle.atddmembership.exception.MembershipErrorCode;
import com.sunyesle.atddmembership.exception.CustomException;
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

    private static final Long USER_ID = 1L;

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
        MembershipType membershipType = MembershipType.NAVER;
        Integer point = 10000;
        MembershipRequest request = new MembershipRequest(membershipType, point);
        given(repository.existsByUserIdAndMembershipType(USER_ID, membershipType))
                .willReturn(false);
        given(repository.save(any()))
                .willReturn(new Membership(1L, USER_ID, membershipType, point));

        // when
        MembershipResponse response = membershipService.createMembership(USER_ID, request);

        // then
        assertThat(response.getMembershipType()).isEqualTo(membershipType);
    }

    @Test
    void 멤버십을_중복으로_등록할_경우_예외가_발생한다(){
        // given
        MembershipType membershipType = MembershipType.NAVER;
        MembershipRequest request = new MembershipRequest(membershipType, 5000);
        given(repository.existsByUserIdAndMembershipType(USER_ID, membershipType))
                .willReturn(true);

        // when then
        assertThatThrownBy(() -> { membershipService.createMembership(USER_ID, request); })
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(MembershipErrorCode.DUPLICATE_MEMBERSHIP.getMessage());
    }

    @Test
    void 멤버십_조회를_성공한다(){
        // given
        Long membershipId = 1L;
        given(repository.findById(membershipId))
                .willReturn(Optional.of(new Membership(membershipId, USER_ID, MembershipType.NAVER, 10000)));

        // when
        MembershipDetailResponse response = membershipService.getMembership(USER_ID, membershipId);

        // then
        assertThat(response.getId()).isEqualTo(membershipId);
    }

    @Test
    void 존재하지_않는_멤버십을_조회할_경우_예외가_발생한다(){
        // given
        Long membershipId = 1L;
        given(repository.findById(membershipId))
                .willReturn(Optional.empty());

        // when then
        assertThatThrownBy(() -> { membershipService.getMembership(USER_ID, membershipId); })
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(MembershipErrorCode.MEMBERSHIP_NOT_FOUND.getMessage());
    }

    @Test
    void 권한이_없는_멤버십을_조회할_경우_예외가_발생한다(){
        // given
        Long membershipId = 1L;
        given(repository.findById(membershipId))
                .willReturn(Optional.of(new Membership(membershipId, 2L, MembershipType.NAVER, 10000)));

        // when then
        assertThatThrownBy(() -> { membershipService.getMembership(USER_ID, membershipId); })
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(MembershipErrorCode.NOT_MEMBERSHIP_OWNER.getMessage());
    }
}

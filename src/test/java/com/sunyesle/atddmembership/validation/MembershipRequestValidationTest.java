package com.sunyesle.atddmembership.validation;

import com.sunyesle.atddmembership.dto.MembershipRequest;
import com.sunyesle.atddmembership.enums.MembershipType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class MembershipRequestValidationTest {

    @Autowired
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void MembershipRequest_유효성_검사_성공() {
        MembershipRequest dto = getValidDto();

        Set<ConstraintViolation<MembershipRequest>> constraintViolations = validator.validate(dto);

        assertThat(constraintViolations).isEmpty();
    }

    static Stream<Arguments> provideFieldAndInvalidValue() {
        return Stream.of(
                Arguments.of("membershipType", null),
                Arguments.of("point", null),
                Arguments.of("point", -1)
        );
    }

    @ParameterizedTest
    @MethodSource("provideFieldAndInvalidValue")
    void MembershipRequest_유효성_검사_실패(String fieldName, Object invalidValue) throws NoSuchFieldException, IllegalAccessException {
        MembershipRequest dto = getValidDto();
        Field field = MembershipRequest.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(dto, invalidValue);

        Set<ConstraintViolation<MembershipRequest>> violations = validator.validate(dto);

        assertThat(violations.size()).isOne();
        System.out.println(dto);
        for (ConstraintViolation<MembershipRequest> violation : violations) {
            System.out.println(violation.getMessage());
        }
    }

    private MembershipRequest getValidDto() {
        return new MembershipRequest(MembershipType.NAVER, 10000);
    }
}

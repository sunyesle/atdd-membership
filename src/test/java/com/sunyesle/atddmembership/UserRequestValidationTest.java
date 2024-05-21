package com.sunyesle.atddmembership;

import com.sunyesle.atddmembership.dto.UserRequest;
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

class UserRequestValidationTest {

    @Autowired
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void UserRequest_유효성_검사_성공() {
        UserRequest dto = getValidDto();

        Set<ConstraintViolation<UserRequest>> constraintViolations = validator.validate(dto);

        assertThat(constraintViolations).isEmpty();
    }

    static Stream<Arguments> provideFieldAndInvalidValue() {
        return Stream.of(
                Arguments.of("username", null),
                Arguments.of("username", "A1"),
                Arguments.of("username", "abcd@123"),
                Arguments.of("password", null),
                Arguments.of("password", "password"),
                Arguments.of("password", "12345678"),
                Arguments.of("password", "pass1"),
                Arguments.of("password", "thisisaverylongpassword123")
        );
    }

    @ParameterizedTest
    @MethodSource("provideFieldAndInvalidValue")
    void UserRequest_유효성_검사_실패(String fieldName, Object invalidValue) throws NoSuchFieldException, IllegalAccessException {
        UserRequest dto = getValidDto();
        Field field = UserRequest.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(dto, invalidValue);

        Set<ConstraintViolation<UserRequest>> violations = validator.validate(dto);

        assertThat(violations.size()).isOne();
        System.out.println(dto);
        for (ConstraintViolation<UserRequest> violation : violations) {
            System.out.println(violation.getMessage());
        }
    }

    private UserRequest getValidDto() {
        return new UserRequest("username1", "passowrd1");
    }
}

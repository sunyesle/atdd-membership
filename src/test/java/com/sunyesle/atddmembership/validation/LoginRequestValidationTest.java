package com.sunyesle.atddmembership.validation;

import com.sunyesle.atddmembership.dto.LoginRequest;
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

class LoginRequestValidationTest {

    @Autowired
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void LoginRequest_유효성_검사_성공() {
        LoginRequest dto = getValidDto();

        Set<ConstraintViolation<LoginRequest>> constraintViolations = validator.validate(dto);

        assertThat(constraintViolations).isEmpty();
    }

    static Stream<Arguments> provideFieldAndInvalidValue() {
        return Stream.of(
                Arguments.of("username", null),
                Arguments.of("password", null)
        );
    }

    @ParameterizedTest
    @MethodSource("provideFieldAndInvalidValue")
    void LoginRequest_유효성_검사_실패(String fieldName, Object invalidValue) throws NoSuchFieldException, IllegalAccessException {
        LoginRequest dto = getValidDto();
        Field field = LoginRequest.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(dto, invalidValue);

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(dto);

        assertThat(violations.size()).isOne();
        System.out.println(dto);
        for (ConstraintViolation<LoginRequest> violation : violations) {
            System.out.println(violation.getMessage());
        }
    }

    private LoginRequest getValidDto() {
        return new LoginRequest("username1", "password1");
    }
}

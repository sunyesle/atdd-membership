package com.sunyesle.atddmembership.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class EnumValidator implements ConstraintValidator<ValidEnum, Enum<?>> {

    @Override
    public boolean isValid(Enum<?> value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return false;
        }
        Class<?> target = value.getDeclaringClass();
        return Arrays.stream(target.getEnumConstants()).anyMatch(v -> v == value);
    }
}

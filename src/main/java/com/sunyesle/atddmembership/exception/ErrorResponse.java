package com.sunyesle.atddmembership.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;

import java.util.List;

@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private final String code;
    private final String message;
    private final List<FieldErrorDto> fieldErrors;

    @Builder
    public ErrorResponse(String code, String message, List<FieldErrorDto> fieldErrors) {
        this.code = code;
        this.message = message;
        this.fieldErrors = fieldErrors;
    }

    public static ErrorResponse of(String code, String message, BindException bindException){
        return new ErrorResponse(code, message, FieldErrorDto.of(bindException));
    }

    @Getter
    @ToString
    public static class FieldErrorDto{
        private final String field;
        private final Object rejectedValue;
        private final String reason;

        public FieldErrorDto(String field, Object rejectedValue, String reason) {
            this.field = field;
            this.rejectedValue = rejectedValue;
            this.reason = reason;
        }
        public static List<FieldErrorDto> of(BindException bindException){
            List<FieldError> fieldErrors = bindException.getFieldErrors();
            return fieldErrors.stream()
                    .map(e-> new FieldErrorDto(
                            e.getField(),
                            e.getRejectedValue() == null ? "" : e.getRejectedValue().toString(),
                            e.getDefaultMessage()))
                    .toList();
        }
    }
}

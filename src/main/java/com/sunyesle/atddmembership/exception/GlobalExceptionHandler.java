package com.sunyesle.atddmembership.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.of("METHOD_ARGUMENT_NOT_VALID", "유효성 검증 실패", ex));
    }

    @ExceptionHandler(MembershipException.class)
    public ResponseEntity<ErrorResponse> handleMembershipException(MembershipException e) {
        log.error("MembershipException", e);
        return createErrorResponseEntity(e.getErrorCode());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e){
        log.error("Exception", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.builder().code("INTERNAL_SERVER_ERROR").message("에러가 발생하였습니다").build());
    }

    private ResponseEntity<ErrorResponse> createErrorResponseEntity(MembershipErrorCode errorCode){
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(createErrorResponse(errorCode));
    }

    private ErrorResponse createErrorResponse(MembershipErrorCode errorCode){
        return ErrorResponse.builder()
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .build();
    }
}

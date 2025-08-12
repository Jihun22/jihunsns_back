// common/handler/GlobalExceptionHandler.java
package com.jihunsns_back.common.handler;

import com.jihunsns_back.common.exception.BusinessException;
import com.jihunsns_back.common.exception.ErrorCode;
import com.jihunsns_back.common.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ErrorResponse> build(ErrorCode code, String message, String path, List<ErrorResponse.FieldError> fields) {
        String traceId = MDC.get("traceId");
        ErrorResponse body = ErrorResponse.of(code.code(), message != null ? message : code.message(), path, traceId, fields);
        return ResponseEntity.status(code.status()).headers(new HttpHeaders()).body(body);
    }

    /* ===== 1) 비즈니스/도메인 예외 ===== */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(BusinessException e, HttpServletRequest req) {
        ErrorCode code = e.getErrorCode();
        log.warn("[BusinessException] code={}, msg={}, path={}, traceId={}", code.code(), e.getMessage(), req.getRequestURI(), MDC.get("traceId"));
        return build(code, e.getMessage(), req.getRequestURI(), Collections.emptyList());
    }

    /* ===== 2) 검증 관련 ===== */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpServletRequest req) {
        List<ErrorResponse.FieldError> fields = e.getBindingResult().getFieldErrors().stream()
                .map(fe -> ErrorResponse.FieldError.of(fe.getField(), fe.getRejectedValue(), fe.getDefaultMessage()))
                .toList();
        log.debug("[Validation] fields={}, path={}, traceId={}", fields, req.getRequestURI(), MDC.get("traceId"));
        return build(ErrorCode.INVALID_INPUT_VALUE, null, req.getRequestURI(), fields);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBind(BindException e, HttpServletRequest req) {
        List<ErrorResponse.FieldError> fields = e.getBindingResult().getFieldErrors().stream()
                .map(fe -> ErrorResponse.FieldError.of(fe.getField(), fe.getRejectedValue(), fe.getDefaultMessage()))
                .toList();
        return build(ErrorCode.INVALID_INPUT_VALUE, null, req.getRequestURI(), fields);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraint(ConstraintViolationException e, HttpServletRequest req) {
        List<ErrorResponse.FieldError> fields = e.getConstraintViolations().stream()
                .map(v -> ErrorResponse.FieldError.of(v.getPropertyPath().toString(), null, v.getMessage()))
                .toList();
        return build(ErrorCode.INVALID_INPUT_VALUE, null, req.getRequestURI(), fields);
    }

    /* ===== 3) HTTP 스펙/요청 형식 ===== */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleBodyRead(HttpMessageNotReadableException e, HttpServletRequest req) {
        return build(ErrorCode.MESSAGE_NOT_READABLE, null, req.getRequestURI(), Collections.emptyList());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleParamMissing(MissingServletRequestParameterException e, HttpServletRequest req) {
        String msg = "필수 파라미터 누락: " + e.getParameterName();
        return build(ErrorCode.PARAMETER_MISSING, msg, req.getRequestURI(), Collections.emptyList());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException e, HttpServletRequest req) {
        return build(ErrorCode.METHOD_NOT_ALLOWED, null, req.getRequestURI(), Collections.emptyList());
    }

    /* ===== 4) 인증/인가 ===== */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuth(AuthenticationException e, HttpServletRequest req) {
        return build(ErrorCode.UNAUTHORIZED, null, req.getRequestURI(), Collections.emptyList());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccess(AccessDeniedException e, HttpServletRequest req) {
        return build(ErrorCode.ACCESS_DENIED, null, req.getRequestURI(), Collections.emptyList());
    }

    /* ===== 5) 데이터 계층 ===== */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException e, HttpServletRequest req) {
        return build(ErrorCode.DATA_INTEGRITY_VIOLATION, null, req.getRequestURI(), Collections.emptyList());
    }

    /* ===== 6) 마지막 보루 ===== */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleEtc(Exception e, HttpServletRequest req) {
        log.error("[Unhandled] type={}, msg={}, path={}, traceId={}", e.getClass().getSimpleName(), e.getMessage(), req.getRequestURI(), MDC.get("traceId"), e);
        return build(ErrorCode.INTERNAL_SERVER_ERROR, null, req.getRequestURI(), Collections.emptyList());
    }
}
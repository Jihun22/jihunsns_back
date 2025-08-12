package com.jihunsns_back.common.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    // 공통
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "요청 값이 유효하지 않습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "C002", "허용되지 않은 메서드입니다."),
    MESSAGE_NOT_READABLE(HttpStatus.BAD_REQUEST, "C003", "요청 바디를 읽을 수 없습니다."),
    PARAMETER_MISSING(HttpStatus.BAD_REQUEST, "C004", "필수 파라미터가 누락되었습니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "C005", "접근 권한이 없습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "C006", "인증이 필요합니다."),
    DATA_INTEGRITY_VIOLATION(HttpStatus.CONFLICT, "C007", "데이터 무결성 제약 조건 오류입니다."),
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "C008", "리소스를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C999", "서버 내부 오류입니다."),

    // 도메인
    USERNAME_DUPLICATED(HttpStatus.CONFLICT, "U001", "이미 사용 중인 아이디입니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "P001", "게시글을 찾을 수 없습니다."),
    LIKE_ALREADY_EXISTS(HttpStatus.CONFLICT, "L001", "이미 좋아요를 누른 상태입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
    public HttpStatus status() { return status; }
    public String code() { return code; }
    public String message() { return message; }

}

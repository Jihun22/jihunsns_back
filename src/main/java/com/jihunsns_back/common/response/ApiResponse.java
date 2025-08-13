package com.jihunsns_back.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(String code, String message, T data) {
    public static <T> ApiResponse<T> ok(T data) { return new ApiResponse<>("S001", "성공", data); }
    public static <T> ApiResponse<T> ok() { return new ApiResponse<>("S001", "성공", null); }
    public static <T> ApiResponse<T> error(String code, String message) { return new ApiResponse<>(code, message, null); }
}
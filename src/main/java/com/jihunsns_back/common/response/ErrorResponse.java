package com.jihunsns_back.common.response;


import java.time.Instant;
import java.util.List;

public record ErrorResponse(
        String code,
        String message,
        String path,
        Instant timestamp,
        String traceId,
        List<FieldError> fieldErrors
) {
    public static ErrorResponse of (String code, String message , String path , String traceId, List<FieldError> fieldErrors ) {
        return new ErrorResponse(code, message, path, Instant.now(), traceId, fieldErrors);
    }
    public record FieldError( String field, String rejectedValue , String reason) {
        public static FieldError of (String field , Object rejectedValue , String reason) {
            return new FieldError(field, rejectedValue== null ? "null" : String.valueOf(rejectedValue), reason);
        }
    }
}

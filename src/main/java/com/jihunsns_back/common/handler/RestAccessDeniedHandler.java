package com.jihunsns_back.common.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jihunsns_back.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

public class RestAccessDeniedHandler  implements AccessDeniedHandler {

    private final ObjectMapper om = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request , HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        var body = ApiResponse.error("AUTH_403", "접근 권한이 없습니다.");
        om.writeValue(response.getOutputStream(), body);
    }
}

package com.jihunsns_back.common.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jihunsns_back.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class RestAuthenticationEntryPoint  implements AuthenticationEntryPoint {

    private final ObjectMapper om = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response ,
                         AuthenticationException authenticationException) throws IOException{
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        var body = ApiResponse.error("AUTH_401" , "인증이 필요합니다.");
        om.writeValue(response.getOutputStream(),body);
    }
}

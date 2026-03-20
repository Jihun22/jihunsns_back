package com.jihunsns_back.security.handler;

/*
 * ===============================================================
 *  File        : Oauth2FailureHandler
 *  Author      : yangjihun
 *  Created On  : 26. 3. 20. 오후 3:58
 * ===============================================================
 */

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @class Oauth2FailureHandler
 * @description Oauth 로그인 실패 핸들러
 * @author yangjihun
 * @since 26. 3. 20.
 */
@Component

public class Oauth2FailureHandler implements AuthenticationFailureHandler {

    @Value("${app.oauth2.authorized-redirect-uri}")

    private String defaultRedirectUri;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException , ServletException{

        String redirectUri = request.getParameter("redirect_uri");
        if (redirectUri == null || redirectUri.isBlank()){
            redirectUri = defaultRedirectUri;
        }
        String targetUrl = redirectUri
                + "?error=" + URLEncoder.encode(exception.getMessage(), StandardCharsets.UTF_8);
        response.sendRedirect(targetUrl);
    }
}
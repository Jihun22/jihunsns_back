package com.jihunsns_back.security.handler;

/*
 * ===============================================================
 *  File        : Oauth2SuccessHandler
 *  Author      : yangjihun
 *  Created On  : 26. 3. 20. 오전 10:39
 * ===============================================================
 */

import com.jihunsns_back.domain.entity.User;
import com.jihunsns_back.domain.repository.UserRepository;
import com.jihunsns_back.security.jwt.JwtProvider;
import com.jihunsns_back.security.oauth2.CustomOauth2User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @class Oauth2SuccessHandler
 * @description Oauth 로그인 성공 핸들러
 * @author yangjihun
 * @since 26. 3. 20.
 */
@Component
@RequiredArgsConstructor
public class Oauth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @Value("${app.oauth2.authorized-redirect-uri}")
    private String defaultRedirectUri;



    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        CustomOauth2User customOauth2User = (CustomOauth2User)  authentication.getPrincipal();
        Long userId = customOauth2User.getUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        String accessToken = jwtProvider.generateAccessToken(user.getId(), user.getEmail(), user.getRole());
        String refreshToken = jwtProvider.generateRefreshToken(user.getId());

        String redirectUri = request.getParameter("redirect_uri");
        if (redirectUri == null || redirectUri.isBlank()) {
            redirectUri = defaultRedirectUri;
        }

        String targetUrl = redirectUri
                + "?accessToken=" + URLEncoder.encode(accessToken, StandardCharsets.UTF_8)
                + "&refreshToken=" + URLEncoder.encode(refreshToken, StandardCharsets.UTF_8);

        response.sendRedirect(targetUrl);

    }

}
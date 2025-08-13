package com.jihunsns_back.security.jwt;

import com.jihunsns_back.security.auth.UserPrincipal;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.http.HttpHeaders;

import java.io.IOException;


public class JwtAuthFilter extends OncePerRequestFilter {

    private  final JwtProvider jwtProvider;

    public JwtAuthFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
        throws ServletException, IOException{
        String auth = req.getHeader(HttpHeaders.AUTHORIZATION);
        if (auth != null && auth.startsWith("Bearer ")) {
            String token = auth.substring(7);
            try {
                Claims claims = jwtProvider.parse(token).getBody();
                Long userId = Long.parseLong(claims.getSubject());
                String role = (String) claims.get("role");
                SecurityContextHolder.getContext().setAuthentication(new UserPrincipal(userId, role));
            } catch (Exception ignored) {
                // 토큰 문제시 인증 미설정(401은 시큐리티가 처리)
            }
        }
        chain.doFilter(req, res);
    }
}
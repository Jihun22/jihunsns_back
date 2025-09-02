package com.jihunsns_back.security.jwt;

import com.jihunsns_back.security.auth.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.util.List;


public class JwtAuthFilter extends OncePerRequestFilter {

    private  final JwtProvider jwtProvider;

    public JwtAuthFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    private static final String[] WHITELIST = {
            "/v3/api-docs", "/v3/api-docs/", "/v3/api-docs.yaml",
            "/swagger-ui", "/swagger-ui/", "/swagger-ui/**",
            "/actuator/health", "/health",
            "/api/auth/", "/api/auth/**" // 회원가입/로그인/리프레시
    };

    private  static final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        for (String p: WHITELIST) {
            if (pathMatcher.match(p,path))
                    return  true;
        }
        return  false;
    }



    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
        throws ServletException, IOException{
        String auth = req.getHeader(HttpHeaders.AUTHORIZATION);
        if (auth != null && auth.startsWith("Bearer ")) {
            String token = auth.substring(7);
            try {
                Jws<Claims> jws = jwtProvider.parse(token);
                Claims c = jws.getBody();
                Long userId = Long.valueOf(c.getSubject());
                String email = c.get("email", String.class);
                String role = c.get("role", String.class);

                var authToken = new UsernamePasswordAuthenticationToken(
                        new UserPrincipal(userId, email, role), // principal
                        null,
                        List.of(new SimpleGrantedAuthority(role))
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } catch (Exception e) {
                // 토큰 오류 → 컨텍스트 비움 (아래 예외 핸들러가 401 처리하게 둠)
                SecurityContextHolder.clearContext();
            }
        }

        chain.doFilter(req, res);
    }

    public record UserPrincipal(Long id, String email, String role) {}
}
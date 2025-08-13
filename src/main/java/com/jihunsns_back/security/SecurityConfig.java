package com.jihunsns_back.security;

import com.jihunsns_back.security.jwt.JwtAuthFilter;
import com.jihunsns_back.security.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    public SecurityConfig(JwtProvider jwtProvider) { this.jwtProvider = jwtProvider; }

    private static final String[] SWAGGER_WHITELIST = {
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/v3/api-docs.yaml"
    };

    // ✅ 1) JWT 필터 빈
    @Bean
    public JwtAuthFilter jwtAuthFilter() {
        return new JwtAuthFilter(jwtProvider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // REST API 기본 설정
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // ✅ 2) 401/403 JSON 응답(선택)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((req, res, e) -> {
                            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            res.setContentType("application/json");
                            res.getWriter().write("""
                        {"code":"C006","message":"인증이 필요합니다.","path":"%s"}
                        """.formatted(req.getRequestURI()));
                        })
                        .accessDeniedHandler((req, res, e) -> {
                            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            res.setContentType("application/json");
                            res.getWriter().write("""
                        {"code":"C005","message":"접근 권한이 없습니다.","path":"%s"}
                        """.formatted(req.getRequestURI()));
                        })
                )

                // 권한 매칭
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // CORS preflight
                        .requestMatchers(SWAGGER_WHITELIST).permitAll()
                        .requestMatchers("/actuator/health", "/health").permitAll()
                        .requestMatchers("/api/auth/**").permitAll() // 로그인/회원가입 등 공개
                        .anyRequest().authenticated()
                )

                // ❌ httpBasic은 제거 (JWT 사용)
                // .httpBasic(Customizer.withDefaults())

                // ✅ 3) JWT 필터 등록 (UsernamePasswordAuthenticationFilter 앞)
                .addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class)
        ;

        // 폼 로그인 미사용(명시적으로 비활성화 하고 싶다면 주석 해제)
        // http.formLogin(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
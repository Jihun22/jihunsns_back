package com.jihunsns_back.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private static final String[] SWAGGER_WHITELIST = {
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/v3/api-docs.yaml"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // REST API 기본 설정
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 예외 처리: 401/403을 JSON으로 떨어뜨리고 싶다면 커스텀 핸들러 연결
                .exceptionHandling(ex -> {
                    // ex.authenticationEntryPoint(...);
                    // ex.accessDeniedHandler(...);
                })

                // 권한 매칭
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // CORS preflight
                        .requestMatchers(SWAGGER_WHITELIST).permitAll()
                        .requestMatchers("/actuator/health", "/health").permitAll()
                        // 인증/회원가입, 아이디 중복검사 등도 공개하려면 여기에 추가
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated()
                )

                // 데모용 기본 인증(추후 JWT 필터로 교체)
                .httpBasic(Customizer.withDefaults())
        // .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) // JWT 쓰면 활성화
        ;

        // 필요 시 formLogin은 명확히 비활성화
        // http.formLogin(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
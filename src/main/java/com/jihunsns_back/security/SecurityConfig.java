// src/main/java/com/jihunsns_back/security/SecurityConfig.java
package com.jihunsns_back.security;

import com.jihunsns_back.security.jwt.JwtAuthFilter;
import com.jihunsns_back.security.jwt.JwtProvider;
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
            "/swagger-ui.html", "/swagger-ui/**",
            "/v3/api-docs", "/v3/api-docs/**", "/v3/api-docs.yaml"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(SWAGGER_WHITELIST).permitAll()
                        .requestMatchers("/actuator/health", "/health").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated()
                )

                .httpBasic(Customizer.withDefaults())

                // ★ JWT 필터 삽입
                .addFilterBefore(new JwtAuthFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
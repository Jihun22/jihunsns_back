package com.jihunsns_back.config.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebCorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000" , "http://127.0.0.1:3000")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS") // ✅ 메서드 허용
                .allowedHeaders("*") // ✅ 요청 헤더 허용 (Authorization 포함)
                .exposedHeaders("Authorization") // (선택) 응답 헤더 노출
                .allowCredentials(true);
    }
}

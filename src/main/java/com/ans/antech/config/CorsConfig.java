package com.ans.antech.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 모든 URL 패턴에 대해 CORS 적용
                .allowedOrigins("http://localhost:5000")  // 허용할 도메인 (React 등 프론트엔드 주소)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // 허용할 HTTP 메서드
                .allowedHeaders("*")  // 모든 헤더 허용
                .allowCredentials(true)  // 인증 정보 포함 허용 (쿠키 등)
                .maxAge(3600);  // pre-flight 요청 캐싱 시간 (초 단위)
    }
}

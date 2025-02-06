package com.ans.antech.service;

import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SentimentService {

    private final RestTemplate restTemplate = new RestTemplate();
    
    // Flask 감정 분석 API 엔드포인트
    private static final String FLASK_URL = "http://localhost:5000/analyze-sentiment";

    /**
     * Flask 서버를 호출하여 뉴스 감정 분석을 수행하는 메서드
     *
     * @param text 분석할 뉴스 요약 (SMR)
     * @return 감정 분석 결과 (긍정, 중립, 부정)
     */
    public Map<String, Object> analyzeSentiment(String text) {
        
        // ✅ 입력 값 검증
        if (text == null || text.trim().isEmpty()) {
            log.warn("⚠ 감정 분석 요청 실패: 분석할 텍스트가 없습니다.");
            return Map.of("error", "분석할 텍스트가 없습니다.");
        }

        // ✅ 요청 데이터(JSON)
        Map<String, String> requestBody = Map.of("text", text);

        // ✅ HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // ✅ 요청 본문 설정
        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

        try {
            log.info("🚀 Flask 감정 분석 요청 시작... 텍스트: {}", text);
            
            // ✅ Flask API 호출
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    FLASK_URL,
                    org.springframework.http.HttpMethod.POST,
                    request,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );

            log.info("✅ Flask 감정 분석 성공! 응답 데이터: {}", response.getBody());
            return response.getBody();

        } catch (Exception e) {
            log.error("❌ Flask 감정 분석 요청 실패: {}", e.getMessage(), e);
            return Map.of("error", "Flask 감정 분석 요청 실패: " + e.getMessage());
        }
    }
}
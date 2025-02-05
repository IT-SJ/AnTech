package com.ans.antech.service;

import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SentimentService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String FLASK_URL = "http://localhost:5000/analyze-sentiment"; // Flask API 주소

    public Map<String, Object> analyzeSentiment(String text) {

        if (text == null || text.isEmpty()) {
            return Map.of("error", "분석할 텍스트가 없습니다.");
        }

        // 요청 데이터(JSON)
        Map<String, String> requestBody = Map.of("text", text);

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 요청 본문 설정
        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

        try {
            // Flask API 호출
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    FLASK_URL,
                    org.springframework.http.HttpMethod.POST,
                    request,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );

            return response.getBody();
        } catch (Exception e) {
            return Map.of("error", "Flask 감정 분석 요청 실패: " + e.getMessage());
        }
    }
}

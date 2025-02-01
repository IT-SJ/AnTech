package com.ans.antech.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
public class WordProcessorService {

    // Spring Boot에서 REST API 호출을 위한 RestTemplate 객체 생성
    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Flask API를 호출하여 Kiwi를 통해 뉴스 요약 데이터를 분석하고 키워드 빈도수를 계산
     * @param text 분석할 뉴스 요약 데이터 (smr 컬럼에서 가져옴)
     * @return 키워드 빈도수를 포함한 Map (JSON 형태)
     */
    public Map<String, Integer> getKeywordFrequency(String text) {
        if (text == null || text.trim().isEmpty()) {
            return Map.of();  // 입력된 텍스트가 없을 경우 빈 맵 반환
        }

        String url = "http://localhost:5001/process-text";  // Flask 서버 주소

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);  // JSON 형식의 요청 설정

        // JSON 형식으로 텍스트 데이터를 Flask에 전송
        HttpEntity<Map<String, String>> request = new HttpEntity<>(Map.of("text", text), headers);

        // Flask API 호출 (POST 요청) → 키워드 분석 결과 반환
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);

        return response.getBody();  // JSON 응답 데이터 반환
    }
}

package com.ans.antech.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ans.antech.mapper.NewsMapper;


@Service
public class WordCloudService {
    @Autowired
    private NewsMapper mapper;

    @Autowired
    private RestTemplate restTemplate; // RestTemplate 주입

    @Value("${flask.url}") // Flask 서버 URL 주입
    private String flaskUrl;

    
    public Map<String, Integer> generateWordCloud() {
        List<String> mainNewsSummaries = getAllMainNewsSummaries();
        List<String> breakingNewsSummaries = getAllBreakingNewsSummaries();
        return getWordFrequencies(mainNewsSummaries, breakingNewsSummaries);
    }

    public List<String> getAllMainNewsSummaries() {

        return mapper.getAllMainNewsSummaries();
    }

    public List<String> getAllBreakingNewsSummaries() {

        return mapper.getAllBreakingNewsSummaries();
    }

    // 성진 - 워드 클라우드 값을 받아오기 위한 Flask와 통신하는 메서드
    // 빈도수 값 받아오는 메서드
    public Map<String, Integer> getWordFrequencies(List<String> mainNewsSummaries, List<String> breakingNewsSummaries) {
        Map<String, Object> requestBody = Map.of(
                "main", mainNewsSummaries,
                "breaking", breakingNewsSummaries);

        // HTTP 요청 엔티티 생성
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody);

        // exchange() 메서드를 사용하여 요청 전송
        return restTemplate.exchange(
                flaskUrl + "/process-text",
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<Map<String, Integer>>() {
                }).getBody();
    }
}

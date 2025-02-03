package com.ans.antech.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ans.antech.mapper.NewsMapper;

@Service
public class HashTagService {
    @Autowired
    private NewsMapper mapper;

    @Value("${flask.url}") // Flask 서버 URL 주입
    private String flaskUrl;

    @Autowired
    private RestTemplate restTemplate; // RestTemplate 주입

    public String getMainNewsContent(int idx) {

        return mapper.findMainNewsContent(idx);
    }

    public String getBreakingNewsContent(int idx) {

        return mapper.findBreakingNewsContent(idx);
    }

    // 주요 뉴스 해시태그 추출
    public List<String> getMainNewsHashtags(String mainNewsContent) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // 요청 본문 생성
        String requestBody = String.format("{\"mainContent\":\"%s\"}", mainNewsContent.replace("\"", "\\\""));
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        // Flask API 호출 및 결과 반환
        return restTemplate.exchange(
                flaskUrl + "/extract-main-hashtags",
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<List<String>>() {
                }).getBody();
    }

    // 속보 뉴스 해시태그 추출
    public List<String> getBreakingNewsHashtags(String breakingNewsContent) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // 요청 본문 생성
        String requestBody = String.format("{\"breakingContent\":\"%s\"}", breakingNewsContent.replace("\"", "\\\""));
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        // Flask API 호출 및 결과 반환
        return restTemplate.exchange(
                flaskUrl + "/extract-breaking-hashtags",
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<List<String>>() {
                }).getBody();
    }

}

package com.ans.antech.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
public class WordCloudService {

    private final RestTemplate restTemplate = new RestTemplate();

    public byte[] generateWordCloud(Map<String, Integer> wordFreq) {
        String url = "http://localhost:5000/generate-wordcloud";  

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Integer>> requestEntity = new HttpEntity<>(wordFreq, headers);

        ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, byte[].class);

        return response.getBody();
    }
}

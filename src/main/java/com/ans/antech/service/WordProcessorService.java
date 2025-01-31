package com.ans.antech.service;

import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WordProcessorService { 
    
    private static final List<String> STOP_WORDS = Arrays.asList("은", "는", "이", "가", "을", "를", "의", "에", "와", "하다");

    public Map<String, Integer> getKeywordFrequency(String text) {
        if (text == null || text.isEmpty()) {
            return Collections.emptyMap();
        }

        // 특수문자 제거 & 공백 기준으로 단어 나누기
        String[] words = text.replaceAll("[^가-힣a-zA-Z0-9]", " ")
                             .toLowerCase()
                             .split("\\s+");

        // 불필요한 단어 제거 & 빈도수 계산
        return Arrays.stream(words)
                .filter(word -> !STOP_WORDS.contains(word))
                .collect(Collectors.toMap(word -> word, word -> 1, Integer::sum));
    }
}
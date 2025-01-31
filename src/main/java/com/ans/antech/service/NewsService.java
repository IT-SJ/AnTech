package com.ans.antech.service;

import com.ans.antech.mapper.NewsMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;



@Service
public class NewsService {
    @Autowired
    private NewsMapper mapper;

    // 주요 뉴스 타이틀 조회
    public List<String> getAllNewsTitles() {
        return mapper.selectTitle();
    }    

    // 속보 뉴스 타이틀 가져오기
    public List<String> getAllBNewsTitles() {
        return mapper.selectBNewsTitle();
    }

    // 은진 `smr`이 부족하면 자동으로 `text`를 사용하여 가져오기 (주요뉴스 + 속보뉴스) (워드클라우드)
    public String getProcessedNewsContent() {
        List<String> newsContentList = mapper.getProcessedNewsContent();
        return String.join(" ", newsContentList);  // 모든 뉴스 내용을 하나의 문자열로 합치기
    }

}

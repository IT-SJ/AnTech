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

}

package com.ans.antech.service;

import com.ans.antech.mapper.NewsMapper;
import com.ans.antech.model.News;

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

    // 메인 뉴스 개수 조회 (페이지네이션 계산용)
    public int getTotalNewsCount() {
        return mapper.countNews();
    }

    // 메인 페이지 뉴스 조회 (페이지네이션 적용)
    public List<News> getNewsByPage(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return mapper.findNewsByPage(pageSize, offset);
    }

    // 속보 뉴스 개수 조회 (페이지네이션 계산용)
    public int getTotalBNewsCount() {
        return mapper.countBNews();
    }

    // 속보 페이지 뉴스 조회 (페이지네이션 적용)
    public List<News> getBNewsByPage(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return mapper.findBNewsByPage(pageSize, offset);
    }
    
}

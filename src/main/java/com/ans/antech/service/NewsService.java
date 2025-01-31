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

    // 전체 뉴스 개수 조회 (페이지네이션 계산용)
    public int getTotalNewsCount() {
        return mapper.countNews();
    }

    // 특정 페이지 뉴스 조회 (페이지네이션 적용)
    public List<News> getNewsByPage(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return mapper.findNewsByPage(pageSize, offset);
    }
    
    // -------------------------검색 페이지 -------------
    // 검색 결과 개수 조회
    public int getTotalSearchCount(String keyword) {
        return mapper.countSearchNews(keyword);
    }

    // 페이지별 뉴스 검색 결과 가져오기
    public List<News> getNewsByKeyword(String keyword, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return mapper.searchNewsWithPagination(keyword, offset, pageSize);
    }
}

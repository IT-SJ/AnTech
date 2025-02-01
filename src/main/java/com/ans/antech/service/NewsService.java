package com.ans.antech.service;

import com.ans.antech.mapper.NewsMapper;
import com.ans.antech.model.News;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    // 은진 뉴스 요약 내용만 가져오기 (워드클라우드)
    public String getProcessedNewsContent() {
        List<String> newsContentList = mapper.getProcessedNewsContent();
        return String.join(" ", newsContentList);  // 모든 뉴스 요약을 하나의 문자열로 합치기
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

    // 분석페이지 --------------------------------------------------------------------------
    public News getNewsById(int idx) {
        return mapper.findNewsById(idx);
    }

    public News getBNewsById(int idx) {
        return mapper.findBNewsById(idx);
    }
}

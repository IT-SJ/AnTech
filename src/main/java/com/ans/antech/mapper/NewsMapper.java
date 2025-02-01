package com.ans.antech.mapper;


import java.util.ArrayList;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ans.antech.model.News;


@Mapper
public interface NewsMapper {
    
    // 주요 뉴스 타이틀 조회
    public List<String> selectTitle();

    // 속보 뉴스 타이틀 조회
    public List<String> selectBNewsTitle();

    // 은진 요약(smr)을 기본으로 사용하고 부족하면 본문(text)을 반환 (워드클라우드 구현)
    List<String> getProcessedNewsContent();  

    // 메인 뉴스(+갯수)
    public int countNews();

    public List<News> findNewsByPage(int pageSize, int offset);
    
    // 속보 뉴스(+갯수)
    public int countBNews();
    
    public List<News> findBNewsByPage(int pageSize, int offset);
    
    // 검색된 뉴스 개수 조회
    int countSearchNews(@Param("keyword") String keyword);
    
    // 페이징 적용하여 뉴스 검색
     List<News> searchNewsWithPagination(@Param("keyword") String keyword,
                                         @Param("offset") int offset,
                                         @Param("pageSize") int pageSize);


    //분석 페이지
    public News findNewsById(@Param("idx") int idx);
    public News findBNewsById(@Param("idx") int idx);
}

package com.ans.antech.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Mapper;

import com.ans.antech.model.News;


@Mapper
public interface NewsMapper {
    
    // 주요 뉴스 타이틀 조회
    public List<String> selectTitle();

    // 속보 뉴스 타이틀 조회
    public List<String> selectBNewsTitle();

    // 메인 뉴스(+갯수)
    public int countNews();
    public List<News> findNewsByPage(int pageSize, int offset);

    // 속보 뉴스(+갯수)
    public int countBNews();
    public List<News> findBNewsByPage(int pageSize, int offset);

}

package com.ans.antech.mapper;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ans.antech.model.News;

@Mapper
public interface NewsMapper {
    
    // 주요 뉴스 타이틀 조회
    public ArrayList<News> selectTitle();

}

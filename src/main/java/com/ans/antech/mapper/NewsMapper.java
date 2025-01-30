package com.ans.antech.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface NewsMapper {
    
    // 주요 뉴스 타이틀 조회
    public List<String> selectTitle();

    // 속보 뉴스 타이틀 조회
    public List<String> selectBNewsTitle();
}

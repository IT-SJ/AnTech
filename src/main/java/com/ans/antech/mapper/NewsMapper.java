package com.ans.antech.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface NewsMapper {
    
    // 주요 뉴스 타이틀 조회
    public List<String> selectTitle();

    // 속보 뉴스 타이틀 조회
    public List<String> selectBNewsTitle();

    // 은진 요약(smr)을 기본으로 사용하고 부족하면 본문(text)을 반환 (워드클라우드 구현)
    List<String> getProcessedNewsContent();  
}

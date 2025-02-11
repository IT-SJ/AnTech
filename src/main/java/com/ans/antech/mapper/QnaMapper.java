package com.ans.antech.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ans.antech.model.Qna;

@Mapper
public interface QnaMapper {
    // 로그인한 사용자의 Q&A 목록 조회
    List<Qna> findQnaByUserId(@Param("userId") String userId);

    void insertQna(Qna qna);
}
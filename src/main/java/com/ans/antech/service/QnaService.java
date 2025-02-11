package com.ans.antech.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ans.antech.mapper.QnaMapper;
import com.ans.antech.model.Qna;

@Service
public class QnaService {

    @Autowired
    private QnaMapper qnaMapper;

    // 로그인한 사용자의 Q&A 목록 조회
    public List<Qna> getQnaListByUserId(String userId) {
        List<Qna> qnaList = qnaMapper.findQnaByUserId(userId);

        // 🛠️ 디버깅 로그 추가
        if (qnaList == null || qnaList.isEmpty()) {
            System.out.println("❌ Qna 목록이 비어 있습니다.");
        } else {
            for (Qna qna : qnaList) {
                System.out.println("✅ Qna 데이터 - idx: " + qna.getIdx() + ", title: " + qna.getTitle());
            }
        }

        return qnaList;
    }

    // 질문 등록
    public void addQna(Qna qna) {
        qnaMapper.insertQna(qna);
    }
}
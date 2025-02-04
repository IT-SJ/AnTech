package com.ans.antech.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ans.antech.mapper.NewsMapper;

@Service
public class ScrapService {

     @Autowired
    private NewsMapper mapper;

    public boolean checkScrap(String id, int idx) {
        return mapper.checkScrap(id, idx) > 0;
    }

    public boolean addScrap(String id, int idx, String type) {
        if ("breaking".equalsIgnoreCase(type)) {
            return mapper.insertScrapBreaking(id, idx) > 0;  // ✅ 속보 뉴스 저장
        } else {
            return mapper.insertScrapMain(id, idx) > 0;  // ✅ 메인 뉴스 저장
        }
    }

    public boolean removeScrap(String id, int idx) {
        return mapper.deleteScrap(id, idx) > 0;
    }
    
    
}

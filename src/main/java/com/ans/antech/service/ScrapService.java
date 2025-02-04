package com.ans.antech.service;

import com.ans.antech.mapper.NewsMapper;
import com.ans.antech.model.News;
import com.ans.antech.model.Scrap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScrapService {

    @Autowired
    private NewsMapper mapper;

    // ✅ 특정 뉴스가 스크랩되어 있는지 확인
    public boolean isScrapped(String id, int idx) {
        return mapper.checkScrap(id, idx) > 0;
    }

    // ✅ 메인 뉴스 스크랩 추가
    public boolean addMainScrap(String id, int mainIdx) {
        Scrap scrap = new Scrap();
        scrap.setId(id);
        scrap.setMain_idx(mainIdx);
        scrap.setBreaking_idx(null);
        scrap.setCreateDt(LocalDateTime.now());

        return mapper.insertScrap(scrap) > 0;
    }

    // ✅ 속보 뉴스 스크랩 추가
    public boolean addBreakingScrap(String id, int breakingIdx) {
        Scrap scrap = new Scrap();
        scrap.setId(id);
        scrap.setMain_idx(null);
        scrap.setBreaking_idx(breakingIdx);
        scrap.setCreateDt(LocalDateTime.now());

        return mapper.insertScrap(scrap) > 0;
    }

    // ✅ 뉴스 스크랩 삭제 (메인/속보 구분 없이 idx만으로 삭제)
    public boolean deleteScrap(String id, int idx) {
        return mapper.deleteScrap(id, idx) > 0;
    }

    // ✅ 특정 사용자의 스크랩한 **메인 뉴스 목록** 가져오기
    public List<Map<String, Object>> getMainScrapNewsList(String id, int page, int pageSize) {
        List<Map<String, Object>> scrapNewsList = new ArrayList<>();
        int offset = (page - 1) * pageSize;

        System.out.println("🔍 getMainScrapNewsList 호출됨! id: " + id + ", page: " + page + ", offset: " + offset);
        List<Scrap> scraps = mapper.getMainScrapList(id, offset, pageSize);

        for (Scrap scrap : scraps) {
            News news = mapper.getMainNewsById(scrap.getMain_idx());
            if (news != null) {
                Map<String, Object> newsData = new HashMap<>();
                newsData.put("idx", news.getIdx());
                newsData.put("title", news.getTitle());
                newsData.put("smr", news.getSmr());
                newsData.put("press", news.getPress());
                newsData.put("type", "main");
                scrapNewsList.add(newsData);
            }
        }
        return scrapNewsList;
    }

    // ✅ 특정 사용자의 스크랩한 **속보 뉴스 목록** 가져오기
    public List<Map<String, Object>> getBreakingScrapNewsList(String id, int page, int pageSize) {
        List<Map<String, Object>> scrapNewsList = new ArrayList<>();
        int offset = (page - 1) * pageSize;

        System.out.println("🔍 getBreakingScrapNewsList 호출됨! id: " + id + ", page: " + page + ", offset: " + offset);
        List<Scrap> scraps = mapper.getBreakingScrapList(id, offset, pageSize);

        for (Scrap scrap : scraps) {
            News news = mapper.getBreakingNewsById(scrap.getBreaking_idx());
            if (news != null) {
                Map<String, Object> newsData = new HashMap<>();
                newsData.put("idx", news.getIdx());
                newsData.put("title", news.getTitle());
                newsData.put("smr", news.getSmr());
                newsData.put("press", news.getPress());
                newsData.put("type", "breaking");
                scrapNewsList.add(newsData);
            }
        }
        return scrapNewsList;
    }

    // ✅ 특정 사용자의 전체 **메인 뉴스 스크랩 개수 조회**
    public int getTotalMainScrapNews(String id) {
        return mapper.getTotalMainScrapNews(id);
    }

    // ✅ 특정 사용자의 전체 **속보 뉴스 스크랩 개수 조회**
    public int getTotalBreakingScrapNews(String id) {
        return mapper.getTotalBreakingScrapNews(id);
    }
}

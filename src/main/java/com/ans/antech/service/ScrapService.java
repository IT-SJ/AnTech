package com.ans.antech.service;

import com.ans.antech.mapper.NewsMapper;
import com.ans.antech.model.News;
import com.ans.antech.model.Scrap;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScrapService {

    @Autowired
    private NewsMapper mapper;

    // 특정 뉴스가 스크랩 되어 있는지 확인
    public boolean isScrapped(String id, String type, int idx) {
        return mapper.checkScrap(id, type, idx) > 0;
    }

    // 뉴스 스크랩 추가
    public boolean addScrap(String id, String type, int idx) {
        Scrap scrap = new Scrap();
        scrap.setId(id);
        if ("breaking".equals(type)) {
            scrap.setBreaking_idx(idx);
        } else {
            scrap.setMain_idx(idx);
        }
        scrap.setCreateDt(LocalDateTime.now());

        return mapper.insertScrap(scrap) > 0;
    }

    // 뉴스 스크랩 삭제
    public boolean deleteScrap(String id, String type, int idx) {
        return mapper.deleteScrap(id, type, idx) > 0;
    }

    // 스크랩 했는지 확인
    public boolean toggleScrap(String id, String type, int idx) {
        if (isScrapped(id, type, idx)) {
            return deleteScrap(id, type, idx);
        } else {
            return addScrap(id, type, idx);
        }
    }

    public List<Map<String, Object>> getScrapNewsList(String id, int page, int pageSize) {
        List<Map<String, Object>> scrapNewsList = new ArrayList<>();
        int offset = (page - 1) * pageSize;

        // ✅ 스크랩 목록 가져오기 전에 로그 찍어보기
        System.out.println("🔍 getScrapNewsList 호출됨! id: " + id + ", page: " + page + ", offset: " + offset);

        List<Scrap> scraps = mapper.getScrapList(id, offset, pageSize);

        // ✅ 가져온 스크랩 데이터 확인
        System.out.println("📝 가져온 scraps 목록: " + scraps);

        if (scraps.isEmpty()) {
            System.out.println("⚠ 스크랩 목록이 비어 있습니다!");
            return scrapNewsList; // 빈 리스트 반환
        }

        for (Scrap scrap : scraps) {
            Map<String, Object> newsData = new HashMap<>();

            if (scrap.getMain_idx() != null) {
                News news = mapper.getMainNewsById(scrap.getMain_idx());
                System.out.println("📌 main_idx=" + scrap.getMain_idx() + " -> 가져온 뉴스: " + news);
                if (news != null) {
                    newsData.put("idx", news.getIdx());
                    newsData.put("title", news.getTitle());
                    newsData.put("smr", news.getSmr());
                    newsData.put("press", news.getPress());
                    newsData.put("type", "main");
                    scrapNewsList.add(newsData);
                }
            }

            if (scrap.getBreaking_idx() != null) {
                News news = mapper.getBreakingNewsById(scrap.getBreaking_idx());
                System.out.println("📌 breaking_idx=" + scrap.getBreaking_idx() + " -> 가져온 뉴스: " + news);
                if (news != null) {
                    newsData.put("idx", news.getIdx());
                    newsData.put("title", news.getTitle());
                    newsData.put("smr", news.getSmr());
                    newsData.put("press", news.getPress());
                    newsData.put("type", "breaking");
                    scrapNewsList.add(newsData);
                }
            }
        }
        return scrapNewsList;
    }

    // ✅ 특정 사용자의 전체 스크랩 뉴스 개수 가져오기
    public int getTotalScrapNews(String id) {
        return mapper.getTotalScrapNews(id); // ✅ Mapper 호출
    }
}
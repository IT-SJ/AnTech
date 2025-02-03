package com.ans.antech.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ans.antech.model.News;
import com.ans.antech.model.Scrap;

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

    // 검색된 뉴스 개수 조회
    int countSearchNews(@Param("keyword") String keyword);

    // 페이징 적용하여 뉴스 검색
    List<News> searchNewsWithPagination(@Param("keyword") String keyword,
            @Param("offset") int offset,
            @Param("pageSize") int pageSize);

    // 분석 페이지
    public News findNewsById(@Param("idx") int idx);

    public News findBNewsById(@Param("idx") int idx);

    // ----------------영빈 즐찾 ----------------------
    // 스크랩 여부 확인
    int checkScrap(@Param("id") String id, @Param("type") String type, @Param("idx") int idx);

    // 스크랩 추가
    int insertScrap(Scrap scrap);

    // 스크랩 삭제
    int deleteScrap(@Param("id") String id, @Param("type") String type, @Param("idx") int idx);

    // ✅ 특정 사용자의 스크랩한 뉴스 목록 조회
    List<News> getScrapNewsByUser(@Param("id") String id, @Param("pageSize") int pageSize, @Param("offset") int offset);

    // ✅ 특정 사용자의 전체 스크랩 뉴스 개수 조회
    int getTotalScrapNews(@Param("id") String id);

    // ✅ 특정 사용자의 스크랩 뉴스 목록 가져오기
    List<Scrap> getScrapList(@Param("id") String id,
            @Param("offset") int offset,
            @Param("pageSize") int pageSize);

    // ✅ 주요 뉴스 ID로 조회
    News getMainNewsById(@Param("idx") int idx);

    // ✅ 속보 뉴스 ID로 조회
    News getBreakingNewsById(@Param("idx") int idx);

    // --------------------------------------------------------------------------
    // 성진 - 워드 클라우드 관련 요약 컬럼 가져오기
    public List<String> getAllMainNewsSummaries();

    public List<String> getAllBreakingNewsSummaries();


    // 성진 - 해시태그 관련 내용 컬럼 가져오기
    public String findMainNewsContent(@Param("idx") int idx);

    public String findBreakingNewsContent(@Param("idx") int idx);
}

package com.ans.antech.service;

import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class CommoditiesService {

    private static final String API_URL = "https://query1.finance.yahoo.com/v8/finance/chart/";
    private final RestTemplate restTemplate = new RestTemplate();

    // 영어 -> 한글 매핑
    private static final Map<String, String> COMMODITY_NAMES = Map.of(
            "Gold", "금",
            "Crude Oil", "원유",
            "Natural Gas", "천연가스",
            "Silver", "은",
            "Copper", "구리");

    /**
     * 주요 원자재 가격 데이터 가져오기 (Gold, Crude Oil, Natural Gas 등)
     * 
     * @return 날짜별 원자재 가격 데이터
     */
    public Map<String, Object> getCommoditiesData() {
        Map<String, Object> commoditiesData = new LinkedHashMap<>();

        // Yahoo Finance에서 조회할 원자재 심볼
        Map<String, String> commodities = Map.of(
                "Gold", "GC=F",
                "Crude Oil", "CL=F",
                "Natural Gas", "NG=F",
                "Silver", "SI=F",
                "Copper", "HG=F");

        try {
            for (Map.Entry<String, String> entry : commodities.entrySet()) {
                String commodityName = entry.getKey();
                String symbol = entry.getValue();
                String apiUrl = API_URL + symbol + "?range=1mo&interval=1d";

                String response = restTemplate.getForObject(apiUrl, String.class);
                JSONObject jsonResponse = new JSONObject(response);

                // 데이터 파싱
                JSONObject meta = jsonResponse.getJSONObject("chart").getJSONArray("result").getJSONObject(0)
                        .getJSONObject("meta");

                double currentPrice = meta.getDouble("regularMarketPrice");
                double previousClose = meta.getDouble("chartPreviousClose");
                double change = currentPrice - previousClose;
                double changePercent = (change / previousClose) * 100;

                // 한글 이름 적용
                String koreanName = COMMODITY_NAMES.getOrDefault(commodityName, commodityName);

                // 데이터 저장
                Map<String, Object> commodityData = new LinkedHashMap<>();
                commodityData.put("name", koreanName);
                commodityData.put("currentPrice", currentPrice);
                commodityData.put("previousClose", previousClose);
                commodityData.put("change", change);
                commodityData.put("changePercent", changePercent);

                commoditiesData.put(koreanName, commodityData);
            }

        } catch (Exception e) {
            commoditiesData.put("error", "API 요청 실패: " + e.getMessage());
        }

        return commoditiesData;
    }
}

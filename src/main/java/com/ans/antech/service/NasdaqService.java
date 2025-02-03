package com.ans.antech.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class NasdaqService {

    private static final String API_URL = "https://query1.finance.yahoo.com/v8/finance/chart/";
    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 나스닥(NASDAQ)과 다우(Dow Jones) 데이터를 가져오는 메서드
     * 
     * @return 날짜별 나스닥 & 다우 지수 데이터
     */
    public Map<String, Object> getNasdaqDowData() {
        Map<String, Object> stockData = new LinkedHashMap<>();
        List<String> dates = new ArrayList<>();
        List<Double> nasdaqPrices = new ArrayList<>();
        List<Double> dowPrices = new ArrayList<>();

        // Yahoo Finance에서 심볼 설정
        String nasdaqUrl = API_URL + "^IXIC" + "?range=1mo&interval=1d"; // 나스닥 (1개월, 1일 간격)
        String dowUrl = API_URL + "^DJI" + "?range=1mo&interval=1d"; // 다우존스 (1개월, 1일 간격)

        try {
            // 나스닥 데이터 가져오기
            String nasdaqResponse = restTemplate.getForObject(nasdaqUrl, String.class);
            JSONObject nasdaqJson = new JSONObject(nasdaqResponse);
            extractStockData(nasdaqJson, dates, nasdaqPrices); // 날짜 포함

            // 다우존스 데이터 가져오기
            String dowResponse = restTemplate.getForObject(dowUrl, String.class);
            JSONObject dowJson = new JSONObject(dowResponse);
            extractStockData(dowJson, null, dowPrices); // 날짜는 이미 저장됨

            // 데이터 맵에 저장
            stockData.put("dates", dates);
            stockData.put("nasdaq", nasdaqPrices);
            stockData.put("dow", dowPrices);
        } catch (Exception e) {
            stockData.put("error", "API 요청 실패: " + e.getMessage());
        }

        return stockData;
    }

    /**
     * JSON 데이터를 파싱하여 날짜와 종가를 리스트에 저장하는 메서드
     * 
     * @param jsonObject Yahoo Finance에서 가져온 JSON 데이터
     * @param dates      날짜 리스트 (최초 호출 시 저장, 이후 호출 시 null)
     * @param prices     종가 리스트
     */
    private void extractStockData(JSONObject jsonObject, List<String> dates, List<Double> prices) {
        if (jsonObject.has("chart")) {
            JSONObject chart = jsonObject.getJSONObject("chart");
            JSONArray results = chart.getJSONArray("result");
            if (results.length() > 0) {
                JSONObject result = results.getJSONObject(0);

                // 날짜(timestamp) 추출
                if (dates != null) {
                    JSONArray timestamps = result.getJSONArray("timestamp");
                    for (int i = 0; i < timestamps.length(); i++) {
                        long timestamp = timestamps.getLong(i) * 1000L; // 초 → 밀리초 변환
                        dates.add(new java.text.SimpleDateFormat("MM-dd")
                                .format(new java.util.Date(timestamp))); // MM-DD 형식 변환
                    }
                }

                // 종가(close price) 추출
                JSONObject indicators = result.getJSONObject("indicators");
                JSONArray quoteArray = indicators.getJSONArray("quote");
                if (quoteArray.length() > 0) {
                    JSONObject quote = quoteArray.getJSONObject(0);
                    JSONArray closePrices = quote.getJSONArray("close");

                    for (int i = 0; i < closePrices.length(); i++) {
                        prices.add(closePrices.optDouble(i, Double.NaN)); // NaN 값 방지
                    }
                }
            }
        }
    }
}

package com.ans.antech.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class StockService {

    private static final String API_URL = "https://query1.finance.yahoo.com/v8/finance/chart/";

    /**
     * 코스피(KOSPI)와 코스닥(KOSDAQ) 데이터를 가져오는 메서드
     * @return 코스피와 코스닥의 날짜별 종가 데이터
     */
    public Map<String, Object> getKospiKosdaqData() {
        Map<String, Object> stockData = new LinkedHashMap<>();
        List<String> dates = new ArrayList<>();
        List<Double> kospiPrices = new ArrayList<>();
        List<Double> kosdaqPrices = new ArrayList<>();

        // Yahoo Finance에서 KOSPI와 KOSDAQ의 심볼
        String kospiUrl = API_URL + "^KS11" + "?range=1mo&interval=1d";
        String kosdaqUrl = API_URL + "^KQ11" + "?range=1mo&interval=1d";

        try {
            RestTemplate restTemplate = new RestTemplate();

            // 코스피 데이터 가져오기
            String kospiResponse = restTemplate.getForObject(kospiUrl, String.class);
            JSONObject kospiJson = new JSONObject(kospiResponse);
            extractStockData(kospiJson, dates, kospiPrices); // 날짜도 함께 저장

            // 코스닥 데이터 가져오기
            String kosdaqResponse = restTemplate.getForObject(kosdaqUrl, String.class);
            JSONObject kosdaqJson = new JSONObject(kosdaqResponse);
            extractStockData(kosdaqJson, null, kosdaqPrices); // 날짜는 이미 저장되었으므로 null 전달

            // 데이터 맵에 저장
            stockData.put("dates", dates);
            stockData.put("kospi", kospiPrices);
            stockData.put("kosdaq", kosdaqPrices);
        } catch (Exception e) {
            stockData.put("error", "API 요청 실패: " + e.getMessage());
        }

        return stockData;
    }

    /**
     * JSON 데이터를 파싱하여 날짜와 종가를 리스트에 저장하는 메서드
     * @param jsonObject Yahoo Finance에서 가져온 JSON 데이터
     * @param dates 날짜 리스트 (최초 호출 시 저장, 이후 호출 시 null)
     * @param prices 종가 리스트
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
                        long timestamp = timestamps.getLong(i) * 1000L;
                        dates.add(new java.text.SimpleDateFormat("yyyy-MM-dd")
                                .format(new java.util.Date(timestamp)));
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
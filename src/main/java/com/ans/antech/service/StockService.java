package com.ans.antech.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class StockService {

    private static final String YAHOO_API = "https://query1.finance.yahoo.com/v8/finance/chart/";
    private static final String KOSPI_SYMBOL = "KS11";
    private static final String KOSDAQ_SYMBOL = "KQ11";

    public Map<String, Object> getKospiKosdaqData() {
        Map<String, Object> response = new HashMap<>();

        JSONObject kospiData = fetchStockData(KOSPI_SYMBOL);
        JSONObject kosdaqData = fetchStockData(KOSDAQ_SYMBOL);

        // JSON 데이터에서 날짜 및 주가 정보 추출
        List<String> dates = extractDates(kospiData);
        List<Double> kospiPrices = extractPrices(kospiData);
        List<Double> kosdaqPrices = extractPrices(kosdaqData);

        response.put("dates", dates);
        response.put("kospi", kospiPrices);
        response.put("kosdaq", kosdaqPrices);
        return response;
    }

    private JSONObject fetchStockData(String symbol) {
        String url = YAHOO_API + symbol + "?range=1mo&interval=1d";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return new JSONObject(response.getBody()).getJSONObject("chart").getJSONArray("result").getJSONObject(0);
    }

    private List<String> extractDates(JSONObject jsonData) {
        JSONArray timestamps = jsonData.getJSONArray("timestamp");

        List<String> dates = new ArrayList<>();
        for (int i = 0; i < timestamps.length(); i++) {
            long timestamp = timestamps.getLong(i) * 1000;
            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date(timestamp));
            dates.add(date);
        }
        return dates;
    }

    private List<Double> extractPrices(JSONObject jsonData) {
        JSONArray prices = jsonData.getJSONObject("indicators")
                .getJSONObject("quote")
                .getJSONArray("close");

        List<Double> priceList = new ArrayList<>();
        for (int i = 0; i < prices.length(); i++) {
            priceList.add(prices.isNull(i) ? 0.0 : prices.getDouble(i));
        }
        return priceList;
    }
}

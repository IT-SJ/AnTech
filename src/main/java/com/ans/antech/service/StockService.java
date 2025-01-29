package com.ans.antech.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class StockService {

    private static final String YAHOO_FINANCE_API = "https://query1.finance.yahoo.com/v8/finance/chart/";

    public List<String> getDates(String stockSymbol) {
        List<String> dates = new ArrayList<>();
        String url = YAHOO_FINANCE_API + stockSymbol + "?range=1mo&interval=1d";

        RestTemplate restTemplate = new RestTemplate();
        try {
            String response = restTemplate.getForObject(url, String.class);
            JSONObject jsonObject = new JSONObject(response);

            // API 응답 확인 (디버깅용)
            System.out.println("API 응답 확인 (dates) : " + jsonObject.toString());

            JSONArray timestamps = jsonObject.getJSONObject("chart")
                    .getJSONArray("result")
                    .getJSONObject(0)
                    .getJSONArray("timestamp");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for (int i = 0; i < timestamps.length(); i++) {
                long timestamp = timestamps.getLong(i) * 1000L; // Unix timestamp 변환
                dates.add(sdf.format(new Date(timestamp)));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ 날짜 데이터 가져오기 실패 : " + e.getMessage());
        }
        return dates;
    }

    public List<Double> getStockData(String stockSymbol) {
        List<Double> prices = new ArrayList<>();
        String url = YAHOO_FINANCE_API + stockSymbol + "?range=1mo&interval=1d";

        RestTemplate restTemplate = new RestTemplate();
        try {
            String response = restTemplate.getForObject(url, String.class);
            JSONObject jsonObject = new JSONObject(response);

            // API 응답 확인 (디버깅용)
            System.out.println("API 응답 확인 (prices) : " + jsonObject.toString());

            JSONArray resultArray = jsonObject.getJSONObject("chart")
                    .getJSONArray("result");

            if (resultArray.length() > 0) {
                JSONObject indicators = resultArray.getJSONObject(0)
                        .getJSONObject("indicators");

                if (indicators.has("quote")) {
                    JSONArray quoteArray = indicators.getJSONArray("quote");
                    if (quoteArray.length() > 0 && quoteArray.getJSONObject(0).has("close")) {
                        JSONArray closePrices = quoteArray.getJSONObject(0).getJSONArray("close");

                        for (int i = 0; i < closePrices.length(); i++) {
                            prices.add(closePrices.isNull(i) ? 0.0 : closePrices.getDouble(i));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ 주가 데이터 가져오기 실패 : " + e.getMessage());
        }
        return prices;
    }
}

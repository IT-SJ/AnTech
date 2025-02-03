package com.ans.antech.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class ExchangeRateService {

    private static final String API_URL = "https://query1.finance.yahoo.com/v8/finance/chart/";
    private final RestTemplate restTemplate = new RestTemplate();

    // Yahoo Finance에서 사용할 환율 심볼 (USD, EUR, JPY, CNY, GBP)
    private static final Map<String, String> EXCHANGE_SYMBOLS = Map.of(
            "미국 USD", "USDKRW=X",
            "유럽 EUR", "EURKRW=X",
            "일본 JPY", "JPYKRW=X",
            "중국 CNY", "CNYKRW=X",
            "영국 GBP", "GBPKRW=X");

    /**
     * 환율 데이터를 가져와 반환
     * 
     * @return 주요 5개국 환율 데이터 (전일 대비 변동 포함)
     */
    public Map<String, Object> getExchangeRates() {
        Map<String, Object> exchangeRates = new LinkedHashMap<>();

        try {
            for (Map.Entry<String, String> entry : EXCHANGE_SYMBOLS.entrySet()) {
                String country = entry.getKey();
                String symbol = entry.getValue();
                String apiUrl = API_URL + symbol + "?range=2d&interval=1d";

                String response = restTemplate.getForObject(apiUrl, String.class);
                JSONObject jsonResponse = new JSONObject(response);

                JSONObject meta = jsonResponse.getJSONObject("chart").getJSONArray("result").getJSONObject(0)
                        .getJSONObject("meta");
                JSONArray closePrices = jsonResponse.getJSONObject("chart").getJSONArray("result").getJSONObject(0)
                        .getJSONObject("indicators").getJSONArray("quote").getJSONObject(0).getJSONArray("close");

                double currentRate = closePrices.length() > 0 ? closePrices.getDouble(closePrices.length() - 1)
                        : meta.getDouble("regularMarketPrice");
                double previousRate = closePrices.length() > 1 ? closePrices.getDouble(closePrices.length() - 2)
                        : meta.getDouble("chartPreviousClose");
                double change = currentRate - previousRate;
                double changePercent = (change / previousRate) * 100;

                // 데이터 저장
                Map<String, Object> rateData = new LinkedHashMap<>();
                rateData.put("name", country);
                rateData.put("currentRate", currentRate);
                rateData.put("previousRate", previousRate);
                rateData.put("change", change);
                rateData.put("changePercent", changePercent);

                exchangeRates.put(country, rateData);
            }

        } catch (Exception e) {
            exchangeRates.put("error", "API 요청 실패: " + e.getMessage());
        }

        return exchangeRates;
    }
}

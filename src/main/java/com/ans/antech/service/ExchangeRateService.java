package com.ans.antech.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class ExchangeRateService {

    private static final String API_KEY = "NoE5mRwQtNs7mxGsVUFp1T9fcOfblZ6I"; // 실제 API 키 입력
    private static final String API_URL = "https://www.koreaexim.go.kr/site/program/financial/exchangeJSON";

    private final RestTemplate restTemplate = new RestTemplate();

     /**
     * 최신 환율 데이터를 가져오는 메서드 (어제 날짜 기준)
     */
    public Map<String, Object> getExchangeRates() {
        Map<String, Object> exchangeRates = new LinkedHashMap<>();

        // 어제 날짜 가져오기 (주말이면 금요일로 변경)
        String searchDate = getPreviousBusinessDay();

        // API 요청 URL 생성
        String apiUrl = API_URL + "?authkey=" + API_KEY + "&searchdate=" + searchDate + "&data=AP01";
        System.out.println("🔗 API 요청 URL: " + apiUrl);

        try {
            // API 요청
            ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);
            System.out.println("📡 응답 상태 코드: " + response.getStatusCode());

            if (response.getStatusCode() == HttpStatus.OK) {
                String rawResponse = response.getBody();
                System.out.println("📦 원본 API 응답 데이터: " + rawResponse);

                JSONArray jsonResponse = new JSONArray(rawResponse);

                // API가 빈 배열을 반환하는 경우
                if (jsonResponse.length() == 0) {
                    System.err.println("❌ 환율 데이터를 찾을 수 없음 (비영업일)");
                    exchangeRates.put("error", "환율 데이터가 제공되지 않았습니다. (주말 또는 공휴일)");
                    return exchangeRates;
                }

                // 주요 환율 저장
                Map<String, String> currencyMapping = Map.of(
                        "USD", "USD/KRW",
                        "JPY(100)", "JPY/KRW",
                        "EUR", "EUR/KRW",
                        "CNH", "CNY/KRW",
                        "GBP", "GBP/KRW"
                );

                for (int i = 0; i < jsonResponse.length(); i++) {
                    JSONObject currencyData = jsonResponse.getJSONObject(i);
                    String currency = currencyData.getString("cur_unit");
                    String dealBasR = currencyData.getString("deal_bas_r");

                    // 매매 기준율 값이 없으면 스킵
                    if (dealBasR == null || dealBasR.isEmpty()) continue;

                    double exchangeRate = Double.parseDouble(dealBasR.replace(",", ""));

                    // 주요 통화에 해당하면 추가
                    if (currencyMapping.containsKey(currency)) {
                        if (currency.equals("JPY(100)")) {
                            exchangeRates.put(currencyMapping.get(currency), exchangeRate / 100); // JPY(100) 보정
                        } else {
                            exchangeRates.put(currencyMapping.get(currency), exchangeRate);
                        }
                    }
                }

                System.out.println("✅ 최종 환율 데이터: " + exchangeRates);
            } else {
                System.err.println("❌ API 요청 실패 (HTTP " + response.getStatusCode() + ")");
                exchangeRates.put("error", "API 요청 실패 (HTTP " + response.getStatusCode() + ")");
            }
        } catch (Exception e) {
            System.err.println("❌ 환율 API 오류: " + e.getMessage());
            exchangeRates.put("error", "API 요청 중 오류 발생: " + e.getMessage());
        }

        return exchangeRates;
    }

    /**
     * 주말(토/일) 또는 공휴일이면 마지막 영업일(금요일)로 설정
     */
    private String getPreviousBusinessDay() {
        LocalDate date = LocalDate.now().minusDays(1); // 어제 날짜

        // 주말이면 금요일로 변경
        if (date.getDayOfWeek() == DayOfWeek.SATURDAY) {
            date = date.minusDays(1); // 금요일
        } else if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            date = date.minusDays(2); // 금요일
        }

        return date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }
}
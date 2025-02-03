package com.ans.antech.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Scrap {
    private int idx; // 자동 증가되는 기본 키
    private String id; // 사용자 ID
    private Integer main_idx; // 주요 뉴스 IDX (NULL 가능)
    private Integer breaking_idx; // 속보 뉴스 IDX (NULL 가능)
    private LocalDateTime createDt; // 생성 날짜
}

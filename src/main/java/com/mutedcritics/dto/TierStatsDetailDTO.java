package com.mutedcritics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 티어 통계 상세 정보 DTO (테이블 행별 데이터)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TierStatsDetailDTO {

    private String tierName; // 티어명
    private String classificationValue; // 분류 값 (예: "남성", "서울", "VIP" 등)
    private long userCount; // 해당 분류의 유저 수

}
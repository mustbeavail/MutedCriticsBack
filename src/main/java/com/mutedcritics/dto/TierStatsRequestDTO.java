package com.mutedcritics.dto;

import lombok.Data;

@Data
public class TierStatsRequestDTO {

    private int seasonIdx;
    private String gender; // 성별 필터
    private String region; // 지역 필터
    private Boolean vip; // VIP 필터
    private Integer heroId; // 메인 영웅 필터
    private String tierName; // 티어 필터
    private int page = 1; // 기본 1페이지
    private int size = 10; // 페이지당 항목 수

    public int getOffset() {
        return (page - 1) * size;
    }

}

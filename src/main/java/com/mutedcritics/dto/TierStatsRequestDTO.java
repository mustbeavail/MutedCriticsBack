package com.mutedcritics.dto;

import lombok.Data;

@Data
public class TierStatsRequestDTO {

    private int seasonId;
    private String gender; // 성별 필터
    private String region; // 지역 필터
    private Boolean vip; // VIP 필터
    private Integer heroId; // 메인 영웅 필터

}

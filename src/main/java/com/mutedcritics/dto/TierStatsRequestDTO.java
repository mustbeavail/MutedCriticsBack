package com.mutedcritics.dto;

import lombok.Data;

@Data
public class TierStatsRequestDTO {

    private int seasonId;
    private String classification;
    private String value;
    private int minPlayTime;
    private int maxPlayTime;

}

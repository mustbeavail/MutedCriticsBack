package com.mutedcritics.dto;

import lombok.Data;

@Data
public class HeroWinRateResponseDTO {
    private String heroName;
    private String role;
    private String tierName;
    private int winCount;
    private int loseCount;
    private int totalMatches;
    private double winRate;
}
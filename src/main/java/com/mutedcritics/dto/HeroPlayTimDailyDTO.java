package com.mutedcritics.dto;

import lombok.Data;

@Data
public class HeroPlayTimDailyDTO {

    private String heroName;
    private String role;
    private int totalPlayTime;
    private int matchCount;
    private double avgPlayTimePerMatch;

}

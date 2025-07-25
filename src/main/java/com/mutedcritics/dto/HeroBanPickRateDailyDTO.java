package com.mutedcritics.dto;

import lombok.Data;

@Data
public class HeroBanPickRateDailyDTO {

    private String heroName;
    private String role;
    private int pickCount;
    private int banCount;
    private int totalGames;
    private double pickRate;
    private double banRate;

}

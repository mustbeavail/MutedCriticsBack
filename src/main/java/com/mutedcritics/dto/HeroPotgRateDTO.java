package com.mutedcritics.dto;

import lombok.Data;

@Data
public class HeroPotgRateDTO {

    private String heroName;
    private String role;
    private int totalMatches;
    private int potgCount;
    private double potgRate;

}
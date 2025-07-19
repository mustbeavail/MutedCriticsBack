package com.mutedcritics.dto;

import lombok.Data;

@Data
public class HeroModeTimeDTO {

    private String heroName;
    private String role;
    private int playTime; // 분 단위
    private int matches;

}

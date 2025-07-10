package com.mutedcritics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeroModeTimeDTO {

    private String matchMode;
    private String heroName;
    private String role;
    private long playTime; // 분 단위
    private int matches;

}

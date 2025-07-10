package com.mutedcritics.dto;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDashBoardDTO {

    private String userId;
    private long wins;
    private long losses;
    private double winRate;
    private long totalPlay;
    private String mainRole;

    private List<HeroModeTimeDTO> heroTimes;
    private Map<String, Long> modePlayTimes;

}

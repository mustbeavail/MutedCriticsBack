package com.mutedcritics.dto;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class UserDashBoardDTO {

    private String userId;
    private int wins;
    private int losses;
    private double winRate;
    private int totalPlay;
    private String mainRole;

    private List<HeroModeTimeDTO> heroTimes;
    private Map<String, Integer> modePlayTimes;

}

package com.mutedcritics.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class UserStatsDTO {

    private String user_id;
    private int season;
    private UserTierDTO tier;




}

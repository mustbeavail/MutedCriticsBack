package com.mutedcritics.dto;

import lombok.Data;

@Data
public class UserStatsSeasonDTO {

    private String user_id;
    private int season;

    private int total_play_time_season;
    private int total_item_price;
    private int total_bundle_price;
    private int total_spending_season;
    private String tier_season;

}

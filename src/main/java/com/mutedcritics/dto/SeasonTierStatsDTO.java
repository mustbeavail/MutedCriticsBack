package com.mutedcritics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeasonTierStatsDTO {

    private String seasonIdx;
    private String seasonName;
    private String tierName;
    private long userCount;

}

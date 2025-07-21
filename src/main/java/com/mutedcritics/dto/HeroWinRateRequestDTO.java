package com.mutedcritics.dto;

import lombok.Data;

@Data
public class HeroWinRateRequestDTO {
    private Integer seasonIdx;
    private String tierName;
    private String sortOrder;
}

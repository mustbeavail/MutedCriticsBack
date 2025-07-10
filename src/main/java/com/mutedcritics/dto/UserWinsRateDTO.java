package com.mutedcritics.dto;

import lombok.Data;

@Data
public class UserWinsRateDTO {

    private String userId;
    private long totalMatches;
    private long wins;
    private long losses;
    private double winsRate;

}

package com.mutedcritics.dto;

import lombok.Data;

@Data
public class ModePlayTimeDTO {

    private String matchMode;
    private int totalPlayTime;
    private int matchCount;
    private double avgPlayTimePerMatch;

}
package com.mutedcritics.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class UserTierDTO {

    private String tier_name;
    private LocalDate tier_date;
    
}

package com.mutedcritics.dto;

import lombok.Data;

@Data
public class UserCategoryDTO {

    private String userId;
    private String userType;
    private String userGender;
    private String region;
    private int totalPlayTime;
    private int totalPayment;
}

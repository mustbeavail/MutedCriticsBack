package com.mutedcritics.dto;

import lombok.Data;

@Data
public class UserListDTO {
    private String userId;
    private String userNick;
    private int totalPlayTime;
    private int totalSpent;
    private String region;
    private String userType;
}

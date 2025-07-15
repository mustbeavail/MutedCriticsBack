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
    private boolean vipYn;

    // 복합 유저 타입 반환 (VIP 여부 포함)
    public String getDisplayUserType() {
        if (vipYn) {
            return userType + " (VIP)";
        }
        return userType;
    }
}

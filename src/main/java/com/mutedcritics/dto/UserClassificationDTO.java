package com.mutedcritics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 유저 분류 정보 DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserClassificationDTO {

    private String userId;
    private String tierName;
    private String userGender; // 성별
    private String region; // 접속지역
    private boolean vipYn; // VIP 여부
    private int age; // 나이 (계산된 값)
    private long totalPlayTime; // 총 플레이타임
    private String mainHero; // 주 플레이 영웅

}
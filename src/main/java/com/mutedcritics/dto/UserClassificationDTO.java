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

    private String userId; // 유저 아이디
    private String tierName; // 티어명
    private String userGender; // 성별
    private String region; // 접속지역
    private long totalPlayTime; // 총 플레이타임
    private long totalPayment; // 총 과금액
    private String mainHero; // 주 플레이 영웅

}
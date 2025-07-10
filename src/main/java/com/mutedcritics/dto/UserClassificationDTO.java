package com.mutedcritics.dto;

import lombok.Data;

/**
 * 유저 분류 정보 DTO
 */
@Data
public class UserClassificationDTO {

    private String userId; // 유저 아이디
    private String tierName; // 티어명
    private String userGender; // 성별
    private String region; // 접속지역
    private int totalPlayTime; // 총 플레이타임
    private int totalPayment; // 총 과금액
    private String mainHero; // 주 플레이 영웅

}
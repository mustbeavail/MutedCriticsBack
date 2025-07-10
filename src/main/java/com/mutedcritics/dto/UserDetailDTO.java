package com.mutedcritics.dto;

import lombok.Data;

@Data
public class UserDetailDTO {

    private String userId; // 유저 아이디
    private String userGender; // 성별
    private String region; // 접속 지역
    private String userType; // 유저 타입
    private int totalPlayTime; // 총 플레이타임 (분)
    private int totalPayment; // 총 과금액
    private String tierName; // 티어명

}
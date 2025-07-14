package com.mutedcritics.activity.service;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.mutedcritics.activity.dao.ActivityDAO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityDAO dao;

    // 총 접속자 수
    public Map<String, Object> total_user() {
        return dao.total_user();
    }

    //일일 접속자 수 조회
    public int getDAU(LocalDate today) {
        return dao.DAU(today);
    }
    //일일 신규 유저 수 조회
    public int getNewUserCount(LocalDate today) {
        return dao.getNewUserCount(today);
    }
    // 일일 휴면 전환 유저 수 조회
    public int getDormantUserCount(LocalDate today) {
        return dao.getDormantUserCount(today);
    }
    // 일일 복귀 유저 수 조회
    public int getReturningUserCount(LocalDate today) {
        return dao.getReturningUserCount(today);
    }
    // 일일 탈퇴 유저 수 조회
    public int getWithdrawnUserCount(LocalDate today) {
        return dao.getWithdrawnUserCount(today);
    }
    
    // 일일 통계 저장
    public boolean insertDailyActivity(Map<String, Object> params) {
        int row = dao.insertDailyActivity(params);
        return row > 0;
    }

}

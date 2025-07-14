package com.mutedcritics.activity.component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.HashMap;
import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mutedcritics.activity.service.ActivityService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ActivityScheduler {

    private final ActivityService service;
    Map<String, Object> params = null;

    // 일일 접속자 수 통계
    @Scheduled(cron = "0 0 9 * * *")
    public void dailyActivityStats() {

        params = new HashMap<>();

        LocalDate today = LocalDate.now();
        params.put("today", today);
        
        int successCount = 0;

        // 일일 접속자 수
        int DAU = service.getDAU(today);
        if (DAU >= 0) {
            params.put("DAU", DAU);
            successCount++;
        } else {
            log.error("DAU 통계 오류: {} 날짜: {}", DAU, today);
            throw new RuntimeException("DAU 통계 오류 " + today);
        }
        // 일일 신규 유저 수
        int newUserCount = service.getNewUserCount(today);
        if (newUserCount >= 0) {
            params.put("newUserCount", newUserCount);
            successCount++;
        } else {
            log.error("신규 유저 수 통계 오류: {} 날짜: {}", newUserCount, today);
            throw new RuntimeException("신규 유저 수 통계 오류 " + today);
        }
        // 일일 휴면 전환 유저 수
        int dormantUserCount = service.getDormantUserCount(today);
        if (dormantUserCount >= 0) {
            params.put("dormantUserCount", dormantUserCount);
            successCount++;
        } else {
            log.error("휴면 전환 유저 수 통계 오류: {} 날짜: {}", dormantUserCount, today);
            throw new RuntimeException("휴면 전환 유저 수 통계 오류 " + today);
        }
        // 일일 복귀 유저 수
        int returningUserCount = service.getReturningUserCount(today);
        if (returningUserCount >= 0) {
            params.put("returningUserCount", returningUserCount);
            successCount++;
        } else {
            log.error("복귀 유저 수 통계 오류: {} 날짜: {}", returningUserCount, today);
            throw new RuntimeException("복귀 유저 수 통계 오류 " + today);
        }
        // 일일 탈퇴 유저 수
        int withdrawnUserCount = service.getWithdrawnUserCount(today);
        if (withdrawnUserCount >= 0) {
            params.put("withdrawnUserCount", withdrawnUserCount);
            successCount++;
        } else {
            log.error("탈퇴 유저 수 통계 오류: {} 날짜: {}", withdrawnUserCount, today);
            throw new RuntimeException("탈퇴 유저 수 통계 오류 " + today);
        }

        // 통계 저장
        boolean success = false;
        if (successCount > 0) {
            success = service.insertDailyActivity(params);
        } else if (successCount != 5) {
            log.warn("누락된 통계 갯수: {} 날짜: {}", 5 - successCount, today);
        } else if (!success) {
            log.error("일일 사용자 활동 통계 저장 오류: {}", successCount, today);
            throw new RuntimeException("일일 사용자 활동 통계 저장 오류 " + today);
        }
    }
}

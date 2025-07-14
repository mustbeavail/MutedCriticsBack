package com.mutedcritics.activity.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityHistoricalService {

    private final ActivityService service;
    private static final int BATCH_SIZE = 60;

    public void processHistoricalStats(LocalDate startDate, LocalDate endDate) {
        LocalDate currentDate = startDate;
        int processedCount = 0;
        boolean success = false;

        while (!currentDate.isAfter(endDate)) {
            for (int i = 0; i < BATCH_SIZE && !currentDate.isAfter(endDate); i++) {
                try {
                    Map<String, Object> params = new HashMap<>();   
                    params.put("today", currentDate);

                    // 기존 ActivityService의 메서드들 그대로 활용
                    Integer DAU = service.getDAU(currentDate);
                    if (DAU != null) params.put("DAU", DAU);
                    
                    Integer newUserCount = service.getNewUserCount(currentDate);
                    if (newUserCount != null) params.put("newUserCount", newUserCount);
                    
                    Integer dormantUserCount = service.getDormantUserCount(currentDate);
                    if (dormantUserCount != null) params.put("dormantUserCount", dormantUserCount);
                    
                    Integer returningUserCount = service.getReturningUserCount(currentDate);
                    if (returningUserCount != null) params.put("returningUserCount", returningUserCount);

                    Integer withdrawnUserCount = service.getWithdrawnUserCount(currentDate);
                    if (withdrawnUserCount != null) params.put("withdrawnUserCount", withdrawnUserCount);

                    // 저장
                    success = service.insertDailyActivity(params);
                    if (!success) {
                        log.error("일일 사용자 활동 통계 저장 오류 날짜: {}", currentDate);
                        throw new RuntimeException("일일 사용자 활동 통계 저장 오류 " + currentDate);
                    }
                    processedCount++;
                    log.info("{}의 통계 처리 완료", currentDate);
                } catch (Exception e) {
                    log.error("통계 계산 오류: {}", e.getMessage());
                    throw new RuntimeException("통계 계산 오류", e);
                }

                currentDate = currentDate.plusDays(1);
            }

            log.info("{}일 처리 완료 (총 {}일 중)", processedCount, ChronoUnit.DAYS.between(startDate, endDate) + 1);

            // 잠시 쉬기 (서버 부하 방지)
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}

package com.mutedcritics.revenue.component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mutedcritics.revenue.service.RevenueService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@EnableScheduling
@Slf4j
@RequiredArgsConstructor
public class RevenueScheduler {

    private final RevenueService service;

    @Scheduled(cron = "0 7 23 * * ?")
    public void dailyRevenueStats() {

        Map<String, Object> params = new HashMap<>();

        LocalDate today = LocalDate.now();
        params.put("today", today);

        int failCount = 0;

        log.info("일일 매출 통계 메서드 실행");

        // 일일 총 매출
        Integer dailyRevenue = service.dailyRevenue(today);
        if (dailyRevenue != null) {
            params.put("dailyRevenue", dailyRevenue);
        }else{
            log.warn("일일 총 매출 조회 실패");
            failCount++;
        }
        // 일일 총 구매건수
        Integer dailyPurchaseCount = service.dailyPurchaseCount(today);
        if (dailyPurchaseCount != null) {
            params.put("dailyPurchaseCount", dailyPurchaseCount);
        }else{
            log.warn("일일 총 구매건수 조회 실패");
            failCount++;
        }
        // 일일 총 PU
        Integer dailyPU = service.dailyPU(today);
        if (dailyPU != null) {
            params.put("dailyPU", dailyPU);
        }else{
            log.warn("일일 총 PU 조회 실패");
            failCount++;
        }
        // 일일 구매주기
        Integer dailyInterval = service.dailyInterval(today);
        if (dailyInterval != null) {
            params.put("dailyInterval", dailyInterval);
        }else{
            log.warn("일일 구매주기 조회 실패");
            failCount++;
        }

        if (failCount == 4) {
            log.warn("모든 일일 매출 통계 1차 조회 실패");
            return;
        }

        // 일일 매출 통계 1차 저장
        boolean success = service.dailyRevenueStats(params);
        
        if (success) {
            log.info("일일 매출 통계 1차 저장 성공");
            failCount = 0;
            success = false;

            // 일일 총 ARPU
            Integer dailyARPU = service.dailyARPU(today);
            if (dailyARPU != null) {
                params.put("dailyARPU", dailyARPU);
            }else{
                log.warn("일일 총 ARPU 조회 실패");
                failCount++;
            }
            // 일일 총 ARPPU
            Integer dailyARPPU = service.dailyARPPU(today);
            if (dailyARPPU != null) {
                params.put("dailyARPPU", dailyARPPU);
            }else{
                log.warn("일일 총 ARPPU 조회 실패");
                failCount++;
            }
            if (failCount == 2) {
                log.warn("모든 일일 매출 통계 2차 조회 실패");
                return;
            }
            // 일일 매출 통계 2차 저장
            success = service.dailyRevenueStats(params);
            if (success) {
                log.info("일일 매출 통계 2차 저장 성공");
            } else {
                log.warn("일일 매출 통계 2차 저장 실패");
            }
        } else {
            log.warn("일일 매출 통계 1차 저장 실패");
        }

    }
}

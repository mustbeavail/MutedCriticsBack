package com.mutedcritics.revenue.service;

import java.time.LocalDate;
import java.util.Map;
import java.util.HashMap;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RevenueHistoricalService {

    private final RevenueService revenueService;
    private static final int BATCH_SIZE = 60;

    public void processHistoricalStats(LocalDate startDate, LocalDate endDate) {
        LocalDate currentDate = startDate;
        int processedCount = 0;

        while (!currentDate.isAfter(endDate)) {
            // 7일치 처리
            for (int i = 0; i < BATCH_SIZE && !currentDate.isAfter(endDate); i++) {
                try {
                    Map<String, Object> params = new HashMap<>();
                    params.put("today", currentDate);
                    
                    // 기존 RevenueService의 메서드들 그대로 활용
                    Integer dailyRevenue = revenueService.dailyRevenue(currentDate);
                    if (dailyRevenue != null) params.put("dailyRevenue", dailyRevenue);
                    
                    Integer dailyPurchaseCount = revenueService.dailyPurchaseCount(currentDate);
                    if (dailyPurchaseCount != null) params.put("dailyPurchaseCount", dailyPurchaseCount);
                    
                    Integer dailyPU = revenueService.dailyPU(currentDate);
                    if (dailyPU != null) params.put("dailyPU", dailyPU);
                    
                    Integer dailyInterval = revenueService.dailyInterval(currentDate);
                    if (dailyInterval != null) params.put("dailyInterval", dailyInterval);

                    // 1차 저장
                    if (revenueService.dailyRevenueStats(params)) {
                        Integer dailyARPU = revenueService.dailyARPU(currentDate);
                        if (dailyARPU != null) params.put("dailyARPU", dailyARPU);
                        
                        Integer dailyARPPU = revenueService.dailyARPPU(currentDate);
                        if (dailyARPPU != null) params.put("dailyARPPU", dailyARPPU);
                        
                        // 2차 저장
                        revenueService.dailyRevenueStats(params);
                        processedCount++;
                        log.info("{}의 통계 처리 완료", currentDate);
                    }
                } catch (Exception e) {
                    log.error("{}의 통계 처리 실패: {}", currentDate, e.getMessage());
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

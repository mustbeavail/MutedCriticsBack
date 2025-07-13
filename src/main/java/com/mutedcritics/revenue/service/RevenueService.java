package com.mutedcritics.revenue.service;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.mutedcritics.revenue.dao.RevenueDAO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RevenueService {

    private final RevenueDAO revenueDAO;

    // 일일 총 매출
    public Integer dailyRevenue(LocalDate today) {
        Integer dailyRevenue = revenueDAO.dailyRevenue(today);
        if (dailyRevenue == null) {
            return null;
        }
        return dailyRevenue;
    }
    // 일일 총 구매건수
    public Integer dailyPurchaseCount(LocalDate today) {
        Integer dailyPurchaseCount = revenueDAO.dailyPurchaseCount(today);
        if (dailyPurchaseCount == null || dailyPurchaseCount < 0) {
            return null;
        }
        return dailyPurchaseCount;
    }
    // 일일 총 PU
    public Integer dailyPU(LocalDate today) {
        Integer dailyPU = revenueDAO.dailyPU(today);
        if (dailyPU == null || dailyPU < 0) {
            return null;
        }
        return dailyPU;
    }
    // 일일 총 ARPU
    public Integer dailyARPU(LocalDate today) {
        Integer dailyARPU = revenueDAO.dailyARPU(today);
        if (dailyARPU == null) {
            return null;
        }
        return dailyARPU;
    }
    // 일일 총 ARPPU
    public Integer dailyARPPU(LocalDate today) {
        Integer dailyARPPU = revenueDAO.dailyARPPU(today);
        if (dailyARPPU == null) {
            return null;
        }
        return dailyARPPU;
    }
    // 일일 구매주기
    public Integer dailyInterval(LocalDate today) {
        Integer dailyInterval = revenueDAO.dailyInterval(today);
        if (dailyInterval == null || dailyInterval < 0) {
            return null;
        }
        return dailyInterval;
    }
    
    // 일일 매출통계 저장
    public boolean dailyRevenueStats(Map<String, Object> params) {
        int row = revenueDAO.dailyRevenueStats(params);
        return row > 0;
    }
}

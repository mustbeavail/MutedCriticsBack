package com.mutedcritics.revenue.dao;

import java.time.LocalDate;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RevenueDAO {

    // 일일 매출통계 저장
    int dailyRevenueStats(Map<String, Object> params);

    // 일일 총 매출
    Integer dailyRevenue(LocalDate today);
    // 일일 총 구매건수
    Integer dailyPurchaseCount(LocalDate today);
    // 일일 총 PU
    Integer dailyPU(LocalDate today);
    // 일일 총 ARPU
    Integer dailyARPU(LocalDate today);
    // 일일 총 ARPPU
    Integer dailyARPPU(LocalDate today);
    // 일일 구매주기
    Integer dailyInterval(LocalDate today);
}

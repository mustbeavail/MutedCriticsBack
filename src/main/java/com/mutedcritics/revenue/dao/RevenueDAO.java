package com.mutedcritics.revenue.dao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
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

    // 기간별 판매액 조회
    List<Map<String, Object>> getRevenuePeriod(String startDate, String endDate);
    // 기간별 ARPU 조회
    List<Map<String, Object>> getARPU(String startDate, String endDate);
    // 기간별 ARPPU 조회
    List<Map<String, Object>> getARPPU(String startDate, String endDate);
    // 기간별 구매주기 조회
    List<Map<String, Object>> getPurchaseInterval(String startDate, String endDate);
    // 기간별 PU 조회
    List<Map<String, Object>> getPU(String startDate, String endDate);
    // 전체 기간 매출통계 조회
    Map<String, Object> getTotal(LocalDate today);

    // ACL 조회
    BigDecimal getACL(LocalDate today);
}

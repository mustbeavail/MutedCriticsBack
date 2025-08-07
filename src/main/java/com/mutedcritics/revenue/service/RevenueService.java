package com.mutedcritics.revenue.service;

import java.time.LocalDate;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.mutedcritics.revenue.dao.RevenueDAO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RevenueService {

    private final RevenueDAO dao;

    // 일일 총 매출
    public Integer dailyRevenue(LocalDate today) {
        return dao.dailyRevenue(today);
    }
    // 일일 총 구매건수
    public Integer dailyPurchaseCount(LocalDate today) {
        Integer dailyPurchaseCount = dao.dailyPurchaseCount(today);
        if (dailyPurchaseCount == null || dailyPurchaseCount < 0) {
            return null;
        }
        return dailyPurchaseCount;
    }
    // 일일 총 PU
    public Integer dailyPU(LocalDate today) {
        Integer dailyPU = dao.dailyPU(today);
        if (dailyPU == null || dailyPU < 0) {
            return null;
        }
        return dailyPU;
    }
    // 일일 총 ARPU
    public Integer dailyARPU(LocalDate today) {
        return dao.dailyARPU(today);
    }
    // 일일 총 ARPPU
    public Integer dailyARPPU(LocalDate today) {
        return dao.dailyARPPU(today);
    }
    // 일일 구매주기
    public Integer dailyInterval(LocalDate today) {
        Integer dailyInterval = dao.dailyInterval(today);
        if (dailyInterval == null || dailyInterval < 0) {
            return null;
        }
        return dailyInterval;
    }
    

    // 일일 매출통계 저장
    public boolean dailyRevenueStats(Map<String, Object> params) {
        int row = dao.dailyRevenueStats(params);
        return row > 0;
    }


    // 기간별 판매액 조회
    public Map<String, Object> getRevenuePeriod(String startDate, String endDate) {

        Map<String, Object> resp = new HashMap<>();

        List<Map<String, Object>> list = dao.getRevenuePeriod(startDate, endDate);

        resp.put("list", list);

        return resp;
    }
    // 기간별 ARPU 조회
    public Map<String, Object> getARPU(String startDate, String endDate) {

        Map<String, Object> resp = new HashMap<>();

        List<Map<String, Object>> list = dao.getARPU(startDate, endDate);

        resp.put("list", list);

        return resp;
    }
    // 기간별 ARPPU 조회
    public Map<String, Object> getARPPU(String startDate, String endDate) {

        Map<String, Object> resp = new HashMap<>();

        List<Map<String, Object>> list = dao.getARPPU(startDate, endDate);

        resp.put("list", list);

        return resp;
    }
    // 기간별 구매주기 조회
    public Map<String, Object> getPurchaseInterval(String startDate, String endDate) {

        Map<String, Object> resp = new HashMap<>();

        List<Map<String, Object>> list = dao.getPurchaseInterval(startDate, endDate);

        resp.put("list", list);

        return resp;
    }
    // 기간별 PU 조회
    public Map<String, Object> getPU(String startDate, String endDate) {

        Map<String, Object> resp = new HashMap<>();

        List<Map<String, Object>> list = dao.getPU(startDate, endDate);

        resp.put("list", list);

        return resp;
    }
    // 전체 기간 매출통계 조회
    public Map<String, Object> getTotal(LocalDate today) {

        return dao.getTotal(today);
    }

    // LTV 조회
    public Map<String, Object> getLTV(String startDate, String endDate) {

        Map<String, Object> resp = new HashMap<>();
        LocalDate today = LocalDate.now();

        // APV 구하기
        long totalRevenue = dao.getRevenuePeriod(startDate, endDate).stream()
            .mapToLong(map -> ((Number) map.get("daily_revenue")).longValue()).sum();
        long totalPurchaseCount = ((Number) dao.getTotal(LocalDate.parse(endDate))
            .get("total_purchase_count")).longValue();
        
        BigDecimal APV = totalPurchaseCount == 0
        ? BigDecimal.ZERO
        : BigDecimal.valueOf(totalRevenue)
                    .divide(BigDecimal.valueOf(totalPurchaseCount), 2, RoundingMode.HALF_UP);
        resp.put("APV", APV);

        // APF 구하기
        long totalPU = dao.getPU(startDate, endDate).stream()
            .mapToLong(map -> ((Number) map.get("daily_paying_users")).longValue()).sum();
        BigDecimal APF = totalPU == 0
        ? BigDecimal.ZERO
        : BigDecimal.valueOf(totalPurchaseCount)
                    .divide(BigDecimal.valueOf(totalPU), 2, RoundingMode.HALF_UP);
        resp.put("APF", APF);

        // ACL 구하기
        BigDecimal ACL = dao.getACL(today).setScale(2, RoundingMode.HALF_UP);
        resp.put("ACL", ACL);

        // LTV 구하기
        BigDecimal LTV = APV.multiply(APF).multiply(ACL).setScale(2, RoundingMode.HALF_UP);
        resp.put("LTV", LTV);

        return resp;
    }
}

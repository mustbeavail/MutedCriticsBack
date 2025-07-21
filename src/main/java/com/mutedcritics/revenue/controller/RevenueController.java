package com.mutedcritics.revenue.controller;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mutedcritics.revenue.service.RevenueHistoricalService;
import com.mutedcritics.revenue.service.RevenueService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RestController
@Slf4j
@RequiredArgsConstructor
public class RevenueController {

    private final RevenueService service;
    private final RevenueHistoricalService historicalService;

    // 이전 통계 일괄 저장
    @PostMapping("/revenue/historical")
    public ResponseEntity<String> calculateHistorical(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        
        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            
            // 비동기로 처리
            CompletableFuture.runAsync(() -> 
                historicalService.processHistoricalStats(start, end));
            
            return ResponseEntity.ok("통계 계산이 시작되었습니다.");
            
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest()
                .body("날짜 형식이 잘못되었습니다. YYYY-MM-DD 형식을 사용해주세요.");
        }
    }

    // 기간별 판매액 조회
    @GetMapping("/revenue/period")
    public Map<String, Object> getRevenuePeriod(
            @RequestParam String startDate,
            @RequestParam String endDate) {

        Map<String, Object> resp = new HashMap<>();

        resp = service.getRevenuePeriod(startDate, endDate);

        return resp;
    }

    // 기간별 ARPU 조회
    @GetMapping("/revenue/ARPU")
    public Map<String, Object> getARPU(
            @RequestParam String startDate,
            @RequestParam String endDate) {

        Map<String, Object> resp = new HashMap<>();

        resp = service.getARPU(startDate, endDate);

        return resp;
    }

    // 기간별 ARPPU 조회
    @GetMapping("/revenue/ARPPU")
    public Map<String, Object> getARPPU(
            @RequestParam String startDate,
            @RequestParam String endDate) {

        Map<String, Object> resp = new HashMap<>();

        resp = service.getARPPU(startDate, endDate);

        return resp;
    }

    // 기간별 평균 구매간격 조회
    @GetMapping("/revenue/purchaseInterval")
    public Map<String, Object> getPurchaseInterval(
            @RequestParam String startDate,
            @RequestParam String endDate) {

        Map<String, Object> resp = new HashMap<>();

        resp = service.getPurchaseInterval(startDate, endDate);

        return resp;
    }

    // 기간별 PU 조회
    @GetMapping("/revenue/PU")
    public Map<String, Object> getPU(
            @RequestParam String startDate,
            @RequestParam String endDate) {

        Map<String, Object> resp = new HashMap<>();

        resp = service.getPU(startDate, endDate);

        return resp;
    }

    // 전체 기간 매출통계 조회
    @GetMapping("/revenue/total")
    public Map<String, Object> getTotal() {

        Map<String, Object> resp = new HashMap<>();
        LocalDate today = LocalDate.now();

        resp = service.getTotal(today);

        return resp;
    }

    // LTV 조회
    @GetMapping("/revenue/LTV")
    public Map<String, Object> getLTV(
            @RequestParam String startDate,
            @RequestParam String endDate) {

        Map<String, Object> resp = new HashMap<>();

        resp = service.getLTV(startDate, endDate);

        return resp;
    }
}

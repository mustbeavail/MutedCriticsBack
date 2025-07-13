package com.mutedcritics.revenue.controller;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mutedcritics.revenue.service.RevenueHistoricalService;
import com.mutedcritics.revenue.service.RevenueService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin
@RestController
@Slf4j
@RequiredArgsConstructor
public class RevenueController {

    private final RevenueService service;
    private final RevenueHistoricalService historicalService;

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

}

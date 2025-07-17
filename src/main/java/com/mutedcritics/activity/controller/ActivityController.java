package com.mutedcritics.activity.controller;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mutedcritics.activity.service.ActivityHistoricalService;
import com.mutedcritics.activity.service.ActivityService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RestController
@Slf4j
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService service;
    private final ActivityHistoricalService historicalService;
    Map<String, Object> resp = null;

    // 이전 통계 일괄 저장
    @PostMapping("/activity/historical")
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

    // 기간별 일일 활성 이용자 수
    @GetMapping("/activity/periodDailyUser/{startDate}/{endDate}")
    public Map<String, Object> periodDailyUser(@PathVariable String startDate, @PathVariable String endDate) {

        resp = new HashMap<>();

        resp = service.periodDailyUser(startDate, endDate);

        return resp;
    }
    
    // 기간별 주간 활성 이용자 수
    @GetMapping("/activity/periodWeeklyUser")
    public Map<String, Object> periodWeeklyUser(
            @RequestParam int fromYear,
            @RequestParam int fromMonth,
            @RequestParam int fromWeek,
            @RequestParam int toYear,
            @RequestParam int toMonth,
            @RequestParam int toWeek) {

        log.info("{}년 {}월 {}주 부터 {}년 {}월 {}주 까지", fromYear, fromMonth, fromWeek, toYear, toMonth, toWeek);

        resp = new HashMap<>();

        resp = service.periodWeeklyUser(fromYear, fromMonth, fromWeek, toYear, toMonth, toWeek);

        return resp;
    }

    // 기간별 월간 활성 이용자 수
    @GetMapping("/activity/periodMonthlyUser")
    public Map<String, Object> periodMonthlyUser(
            @RequestParam int fromYear,
            @RequestParam int fromMonth,
            @RequestParam int toYear,
            @RequestParam int toMonth) {
                
        log.info("{}년 {}월 부터 {}년 {}월 까지", fromYear, fromMonth, toYear, toMonth);
        return service.periodMonthlyUser(fromYear, fromMonth, toYear, toMonth);
    }
}

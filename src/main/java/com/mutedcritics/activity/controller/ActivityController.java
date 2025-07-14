package com.mutedcritics.activity.controller;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mutedcritics.activity.service.ActivityHistoricalService;
import com.mutedcritics.activity.service.ActivityService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin
@RestController
@Slf4j
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService service;
    private final ActivityHistoricalService historicalService;
    Map<String, Object> params = null;

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

/*     // 총 접속자 수
    @GetMapping("/activity/total_user")
    public Map<String, Object> total_user() {
        Map<String, Object> result = new HashMap<String, Object>();
        result = service.total_user();
        return result;
    }

    // 오늘 날짜 기준 총 일일 접속자 수
    @GetMapping("/activity/today_daily_user")
    public Map<String, Object> today_daily_user() {
        Map<String, Object> result = new HashMap<String, Object>();
        result = service.today_daily_user();
        return result;
    }

    // 오늘 날짜 기준 총 주간 접속자 수
    @GetMapping("/activity/today_weekly_user")
    public Map<String, Object> today_weekly_user() {
        Map<String, Object> result = new HashMap<String, Object>();
        result = service.today_weekly_user();
        return result;
    }

    // 오늘 날짜 기준 총 월간 접속자 수
    @GetMapping("/activity/today_monthly_user")
    public Map<String, Object> today_monthly_user() {
        Map<String, Object> result = new HashMap<String, Object>();
        result = service.today_monthly_user();
        return result;
    }

    // 기간별 일일 활성 이용자 수
    @GetMapping("/activity/period_daily_user/{start_date}/{end_date}")
    public Map<String, Object> period_daily_user(@PathVariable String start_date, @PathVariable String end_date) {
        Map<String, Object> result = new HashMap<String, Object>();
        result = service.period_daily_user(start_date, end_date);
        return result;
    }

    // 기간별 주간 활성 이용자 수
    @GetMapping("/activity/period_weekly_user")
    public Map<String, Object> period_weekly_user(
            @RequestParam int fromYear,
            @RequestParam int fromMonth,
            @RequestParam int fromWeek,
            @RequestParam int toYear,
            @RequestParam int toMonth,
            @RequestParam int toWeek) {

        log.info("{}년 {}월 {}주 부터 {}년 {}월 {}주 까지", fromYear, fromMonth, fromWeek, toYear, toMonth, toWeek);
        return service.period_weekly_user(fromYear, fromMonth, fromWeek, toYear, toMonth, toWeek);
    }

    // 기간별 월간 활성 이용자 수
    @GetMapping("/activity/period_monthly_user")
    public Map<String, Object> period_monthly_user(
            @RequestParam int fromYear,
            @RequestParam int fromMonth,
            @RequestParam int toYear,
            @RequestParam int toMonth) {
                
        log.info("{}년 {}월 부터 {}년 {}월 까지", fromYear, fromMonth, toYear, toMonth);
        return service.period_monthly_user(fromYear, fromMonth, toYear, toMonth);
    } */

}

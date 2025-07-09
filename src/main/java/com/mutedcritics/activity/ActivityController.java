package com.mutedcritics.activity;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin
@RestController
@Slf4j
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService service;

    // 총 접속자 수
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
    }

}

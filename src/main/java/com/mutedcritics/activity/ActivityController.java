package com.mutedcritics.activity;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mutedcritics.utils.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import javax.servlet.http.HttpServletRequest;

@CrossOrigin
@RestController
@Slf4j
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService service;

    // 총 접속자 수
    @GetMapping("/activity/total_user")
    public Map<String, Object> total_user(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        result = service.total_user();
        return result;
    }

    // 오늘 날짜 기준 총 일일 접속자 수
    @GetMapping("/activity/today_daily_user")
    public Map<String, Object> today_daily_user(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        result = service.today_daily_user();
        return result;
    }

    // 오늘 날짜 기준 총 주간 접속자 수
    @GetMapping("/activity/today_weekly_user")
    public Map<String, Object> weekly_user(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        result = service.today_weekly_user();
        return result;
    }

    // 오늘 날짜 기준 총 월간 접속자 수
    @GetMapping("/activity/today_monthly_user")
    public Map<String, Object> today_monthly_user(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        result = service.today_monthly_user();
        return result;
    }

}

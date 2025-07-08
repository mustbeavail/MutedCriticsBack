package com.mutedcritics.activity;

import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class ActivityService {

    private final ActivityDAO dao;

    public ActivityService(ActivityDAO dao) {
        this.dao = dao;
    }

    // 총 접속자 수
    public Map<String, Object> total_user() {
        return dao.total_user();
    }

    // 오늘 날짜 기준 총 일일 접속자 수
    public Map<String, Object> today_daily_user() {
        return dao.today_daily_user();
    }

    // 오늘 날짜 기준 총 주간 접속자 수
    public Map<String, Object> today_weekly_user() {
        return dao.today_weekly_user();
    }

    // 오늘 날짜 기준 총 월간 접속자 수
    public Map<String, Object> today_monthly_user() {
        return dao.today_monthly_user();
    }

}

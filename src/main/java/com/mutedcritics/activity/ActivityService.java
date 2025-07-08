package com.mutedcritics.activity;

import java.time.LocalDate;
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

    // 기간별 일일 활성 이용자 수
    public Map<String, Object> period_daily_user(String start_date, String end_date) {
        return dao.period_daily_user(start_date, end_date);
    }

    // 기간별 주간 활성 이용자 수
    public Map<String, Object> period_weekly_user(int fromYear, int fromMonth, int fromWeek, int toYear, int toMonth, int toWeek) {
        LocalDate fromDate = WeekDateRange.getWeekRange(fromYear, fromMonth, fromWeek)[0];
        LocalDate toDate = WeekDateRange.getWeekRange(toYear, toMonth, toWeek)[1];
        return dao.period_weekly_user(fromDate, toDate);
    }

}

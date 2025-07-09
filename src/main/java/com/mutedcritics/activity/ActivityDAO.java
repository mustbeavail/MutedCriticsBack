package com.mutedcritics.activity;

import java.time.LocalDate;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ActivityDAO {

    // 총 접속자 수
    Map<String, Object> total_user();

    // 오늘 날짜 기준 총 일일 접속자 수
    Map<String, Object> today_daily_user();

    // 오늘 날짜 기준 총 주간 접속자 수
    Map<String, Object> today_weekly_user();

    // 오늘 날짜 기준 총 월간 접속자 수
    Map<String, Object> today_monthly_user();

    // 기간별 일일 활성 이용자 수
    Map<String, Object> period_daily_user(String start_date, String end_date);

    // 기간별 주간 활성 이용자 수
    Map<String, Object> period_weekly_user(LocalDate fromDate, LocalDate toDate);

    // 기간별 월간 활성 이용자 수
    Map<String, Object> period_monthly_user(LocalDate fromDate, LocalDate toDate);

}

package com.mutedcritics.activity;

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

}

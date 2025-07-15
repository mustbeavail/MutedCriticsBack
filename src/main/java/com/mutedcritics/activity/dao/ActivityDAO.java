package com.mutedcritics.activity.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ActivityDAO {

    // 총 접속자 수
    Map<String, Object> total_user();

    // 일일 접속자 수 조회
    int DAU(@Param("today") LocalDate today);
    // 일일 신규 유저 수 조회
    int getNewUserCount(@Param("today") LocalDate today);
    // 일일 휴면 전환 유저 수 조회
    int getDormantUserCount(@Param("today") LocalDate today);
    // 일일 복귀 유저 수 조회
    int getReturningUserCount(@Param("today") LocalDate today);
    // 일일 탈퇴 유저 수 조회
    int getWithdrawnUserCount(@Param("today") LocalDate today);
    // 일일 통계 저장
    int insertDailyActivity(Map<String, Object> params);

    // 기간별 일일 활성 이용자 수
    List<Map<String, Object>> periodDailyUser(@Param("startDate") String startDate, @Param("endDate") String endDate);
    // 기간별 주간 활성 이용자 수
    List<Map<String, Object>> periodWeeklyUser(@Param("weeklyDates") Map<String, List<LocalDate>> weeklyDates);
    // 기간별 월간 활성 이용자 수
    List<Map<String, Object>> periodMonthlyUser(@Param("monthlyDates") Map<String, List<LocalDate>> monthlyDates);

}

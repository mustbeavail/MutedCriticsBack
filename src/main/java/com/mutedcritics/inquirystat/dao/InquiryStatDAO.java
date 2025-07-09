package com.mutedcritics.inquirystat.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDate;

@Mapper
public interface InquiryStatDAO {

    // 특정 날짜의 일일 통계 데이터 업데이트
    void updateDailyStats(@Param("targetDate") LocalDate targetDate);

    // 기간별 일일 통계 데이터 일괄 삽입
    void insertDailyStatsBatch(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}

package com.mutedcritics.inquirystat.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface InquiryStatDAO {

    // 문의/신고 상태 변경 시 미처리 건수 업데이트 (효율적인 실시간 업데이트)
    int updateUnresolvedCount(@Param("statsDate") LocalDate statsDate,
            @Param("ticketType") String ticketType,
            @Param("category") String category);

    // 특정 날짜의 기존 통계 데이터 전체 재계산 (수동 업데이트용)
    int updateDailyStats(@Param("targetDate") LocalDate targetDate);

    // 특정 날짜의 새로운 통계 데이터 삽입 (존재하지 않는 경우만)
    int insertNewDailyStats(@Param("targetDate") LocalDate targetDate);

    // 특정 날짜의 통계 데이터 존재 여부 확인
    int checkStatsExists(@Param("targetDate") LocalDate targetDate);

    // 기간별 일일 통계 데이터 일괄 삽입
    void insertDailyStatsBatch(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // === 조회 메서드 ===

    // 전체 신고/문의 건수 조회
    List<Map<String, Object>> getAllTicketStats();

    // 기간별 일별 신고/문의 건수 조회 (그래프용)
    List<Map<String, Object>> getTicketCountsByPeriod(@Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);



}

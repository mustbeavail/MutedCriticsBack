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

    // 기간별 일일 통계 데이터 일괄 삽입
    void insertDailyStatsBatch(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /* 조회 API */
    // 전체 신고/문의 건수 조회
    List<Map<String, Object>> getAllTicketStats();

    // 기간별 일별 신고/문의 건수 조회
    List<Map<String, Object>> getTicketCountsByPeriod(@Param("startDate") LocalDate startDate,
                                                      @Param("endDate") LocalDate endDate);

}

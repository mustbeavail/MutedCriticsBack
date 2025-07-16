package com.mutedcritics.inquirystat.service;

import com.mutedcritics.inquirystat.dao.InquiryStatDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class InquiryStatService {

    private final InquiryStatDAO dao;

    // 문의/신고 상태 변경 시 미처리 건수 업데이트 (효율적인 실시간 업데이트)
    @Transactional
    public void updateUnresolvedCountAuto(LocalDate statsDate, String ticketType, String category) {
        log.info("미처리 건수 자동 업데이트 시작: {} - {} - {}", statsDate, ticketType, category);

        try {
            int updatedCount = dao.updateUnresolvedCount(statsDate, ticketType, category);

            if (updatedCount > 0) {
                log.info("미처리 건수 업데이트 완료: {} - {} - {} (업데이트된 레코드 수: {})",
                        statsDate, ticketType, category, updatedCount);
            } else {
                log.warn("업데이트할 통계 데이터가 없음: {} - {} - {} (해당 조합의 통계 데이터가 존재하지 않거나 이미 0)",
                        statsDate, ticketType, category);

            }

        } catch (Exception e) {
            log.error("미처리 건수 자동 업데이트 실패: {} - {} - {}", statsDate, ticketType, category, e);
            throw new RuntimeException("미처리 건수 자동 업데이트 실패", e);
        }
    }

    // 기간별 일일 통계 데이터 일괄 생성
    @Transactional
    public void generateDailyStatsBatch(LocalDate startDate, LocalDate endDate) {
        log.info("일일 통계 데이터 일괄 생성 시작: {} ~ {}", startDate, endDate);

        try {
            dao.insertDailyStatsBatch(startDate, endDate);
            log.info("일일 통계 데이터 일괄 생성 완료: {} ~ {}", startDate, endDate);
        } catch (Exception e) {
            log.error("일일 통계 데이터 일괄 생성 실패: {} ~ {}", startDate, endDate, e);
            throw new RuntimeException("일일 통계 데이터 생성 중 오류가 발생했습니다.", e);
        }
    }

    /* 조회 API */
    // 전체 신고/문의 건수 조회
    public List<Map<String, Object>> getAllTicketStats() {
        log.info("전체 신고/문의 건수 조회");
        return dao.getAllTicketStats();
    }

    // 기간별 일별 신고/문의 건수 조회
    public List<Map<String, Object>> getTicketCountsByPeriod(LocalDate startDate, LocalDate endDate) {
        log.info("기간별 일별 신고/문의 건수 조회: {} ~ {}", startDate, endDate);
        return dao.getTicketCountsByPeriod(startDate, endDate);
    }


}

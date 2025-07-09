package com.mutedcritics.inquirystat.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mutedcritics.inquirystat.dao.InquiryStatDAO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class InquiryStatService {

    private final InquiryStatDAO dao;

    // 특정 날짜의 일일 통계 자동 업데이트
    @Transactional
    public void updateDailyStatsAuto(LocalDate targetDate) {
        log.info("일일 통계 자동 업데이트 시작: {}", targetDate);

        try {
            dao.updateDailyStats(targetDate);
            log.info("일일 통계 자동 업데이트 완료: {}", targetDate);
        } catch (Exception e) {
            log.error("일일 통계 자동 업데이트 실패: {}", targetDate, e);
            // 통계 업데이트 실패가 메인 비즈니스 로직에 영향을 주지 않도록 예외를 던지지 않음
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
}

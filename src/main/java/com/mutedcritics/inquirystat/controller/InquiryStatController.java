package com.mutedcritics.inquirystat.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mutedcritics.inquirystat.service.InquiryStatService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@Slf4j
@RequiredArgsConstructor
public class InquiryStatController {

    private final InquiryStatService service;

    // 기간별 일일 통계 일괄 생성 (1월 1일 ~ 7월 5일)
    @PostMapping("/inquiry/daily-stat-batch")
    public Map<String, Object> generateDailyStatsBatch(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        Map<String, Object> result = new HashMap<>();

        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);

            service.generateDailyStatsBatch(start, end);

            result.put("success", true);
            result.put("message", "기간별 일일 통계 데이터가 성공적으로 생성되었습니다.");
            result.put("startDate", startDate);
            result.put("endDate", endDate);

        } catch (Exception e) {
            log.error("기간별 일일 통계 생성 실패: {} ~ {}", startDate, endDate, e);
            result.put("success", false);
            result.put("message", "기간별 일일 통계 생성 중 오류가 발생했습니다: " + e.getMessage());
        }

        return result;
    }

    // 특정 날짜의 일일 통계 수동 업데이트 (테스트용)
    @PostMapping("/inquiry/daily-stat")
    public Map<String, Object> updateDailyStats(@RequestParam String targetDate) {
        Map<String, Object> result = new HashMap<>();

        try {
            LocalDate date = LocalDate.parse(targetDate);
            service.updateDailyStatsAuto(date);

            result.put("success", true);
            result.put("message", "일일 통계 데이터가 성공적으로 업데이트되었습니다.");
            result.put("targetDate", targetDate);

        } catch (Exception e) {
            log.error("일일 통계 업데이트 실패: {}", targetDate, e);
            result.put("success", false);
            result.put("message", "일일 통계 업데이트 중 오류가 발생했습니다: " + e.getMessage());
        }

        return result;
    }
}

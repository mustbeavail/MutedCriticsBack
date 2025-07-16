package com.mutedcritics.inquirystat.controller;

import com.mutedcritics.inquirystat.service.InquiryStatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@Slf4j
@RequiredArgsConstructor
public class InquiryStatController {

    private final InquiryStatService service;

    // 일일 통계 일괄 생성
    @PostMapping("/inquiry/daily-stat-batch")
    public Map<String, Object> generateDailyStatsBatch(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        Map<String, Object> result = new HashMap<>();

        try {
            service.generateDailyStatsBatch(startDate, endDate);

            result.put("success", true);
            result.put("msg", "일일 통계 데이터가 성공적으로 생성되었습니다.");
            result.put("startDate", startDate);
            result.put("endDate", endDate);

        } catch (Exception e) {
            log.error("일일 통계 생성 실패: {} ~ {}", startDate, endDate, e);
            result.put("success", false);
            result.put("msg", "일일 통계 생성 중 오류가 발생했습니다: " + e.getMessage());
        }

        return result;
    }

    /* 조회 API */
    // 전체 신고/문의 건수 조회
    @GetMapping("/inquiry/stats/all")
    public Map<String, Object> getAllTicketStats() {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> stats = service.getAllTicketStats();
        result.put("success", true);
        result.put("list", stats);
        return result;
    }

    // 기간별 일별 신고/문의 건수 조회
    @GetMapping("/inquiry/stats")
    public Map<String, Object> getTicketStats(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        Map<String, Object> result = new HashMap<>();

        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);

            List<Map<String, Object>> stats = service.getTicketCountsByPeriod(start, end);

            result.put("success", true);
            result.put("data", stats);
            result.put("startDate", startDate);
            result.put("endDate", endDate);
            result.put("msg", "기간별 신고/문의 건수 조회가 완료되었습니다.");

        } catch (Exception e) {
            log.error("기간별 신고/문의 건수 조회 실패: {} ~ {}", startDate, endDate, e);
            result.put("success", false);
            result.put("msg", "기간별 신고/문의 건수 조회 중 오류가 발생했습니다: " + e.getMessage());
        }

        return result;
    }

    // 새벽 1시마다 일일 통계 업데이트
    @Scheduled(cron = "0 0 1 * * *")
    public void dailyStatsBatchAuto() {
        LocalDate start = LocalDate.now().minusDays(1);
        LocalDate end = LocalDate.now().minusDays(1);
        log.info("신고/문의 일일 통계 자동 업데이트 시작...");
        service.generateDailyStatsBatch(start, end);
        log.info("신고/문의 일일 통계 자동 업데이트 완료!");
    }
}

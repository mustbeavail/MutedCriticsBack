package com.mutedcritics.ingame;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mutedcritics.ingame.service.IngameService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin
@RestController
@Slf4j
@RequiredArgsConstructor
public class IngameController {

    private final IngameService service;

    // 기본 상수
    private final LocalDate startDate = LocalDate.of(2025, 1, 1);
    private final LocalDate endDate = LocalDate.of(2026, 1, 26);

    /**
     * 전체 기간 승리/패배 횟수 insert
     * 
     * @param startDate
     * @param endDate
     * @return
     */
    @PostMapping("/insert-win-lose-count")
    public Map<String, Object> insertWinLoseCount() {
        Map<String, Object> result = new HashMap<>();
        log.info("전체 기간 승리/패배 횟수 계산 시작 : {} ~ {}", startDate, endDate);
        try {
            int count = service.insertWinLoseCount(startDate, endDate);
            result.put("success", true);
            result.put("msg", "전체 기간 승리/패배 횟수 계산 완료");
            result.put("rowCount", count);
            result.put("startDate", startDate.toString());
            result.put("endDate", endDate.toString());
        } catch (Exception e) {
            log.error("전체 기간 승리/패배 횟수 계산 실패 : {}", e.getMessage());
            result.put("success", false);
            result.put("msg", e.getMessage());
        }
        return result;
    }

    /**
     * 전체 기간 픽 횟수 insert
     * 
     * @param startDate
     * @param endDate
     * @return
     */
    @PostMapping("/insert-pick-count")
    public Map<String, Object> insertPickCount() {
        Map<String, Object> result = new HashMap<>();
        log.info("전체 기간 픽 횟수 계산 시작 : {} ~ {}", startDate, endDate);
        try {
            int count = service.insertPickCount(startDate, endDate);
            result.put("success", true);
            result.put("msg", "전체 기간 픽 횟수 계산 완료");
            result.put("rowCount", count);
            result.put("startDate", startDate.toString());
            result.put("endDate", endDate.toString());
        } catch (Exception e) {
            log.error("전체 기간 픽 횟수 계산 실패 : {}", e.getMessage());
            result.put("success", false);
            result.put("msg", e.getMessage());
        }
        return result;
    }

}

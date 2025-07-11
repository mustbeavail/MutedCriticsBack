package com.mutedcritics.ingame.controller;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    /**
     * 일일 영웅 통계 업데이트
     * 
     * @param startDate
     * @param endDate
     * @return
     */
    @PostMapping("/insert/daily-hero-stats")
    public ResponseEntity<?> insertDailyHeroStats(@RequestBody Map<String, Object> param) {
        LocalDate startDate = LocalDate.parse(param.get("startDate").toString());
        LocalDate endDate = LocalDate.parse(param.get("endDate").toString());
        service.insertDailyHeroStats(startDate, endDate);
        return ResponseEntity.ok(startDate + " ~ " + endDate + " 날짜의 일일 영웅 통계가 업데이트 되었습니다.");
    }
}

package com.mutedcritics.userstat.controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mutedcritics.userstat.service.UserStatService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 유저 통계 관련 API 요청을 처리하는 컨트롤러 클래스.
 * 유저 대시보드 정보를 제공합니다.
 */

@RestController
@Slf4j
@RequiredArgsConstructor
public class UserStatController {

    private final UserStatService service;

    /**
     * 특정 유저의 대시보드 정보를 조회하는 GET 요청을 처리합니다.
     *
     * @param userId    조회할 유저의 ID
     * @param startDate 조회 기간의 시작 날짜 (ISO 형식: YYYY-MM-DD)
     * @param endDate   조회 기간의 종료 날짜 (ISO 형식: YYYY-MM-DD)
     * @return 유저 대시보드 정보를 담은 ResponseEntity
     * @throws IllegalArgumentException startDate가 endDate보다 늦을 경우 발생
     */
    @GetMapping("/user-stats/overview")
    public ResponseEntity<?> getUserStatsOverview(
            @RequestParam String userId,
            @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate endDate) {
        log.info("유저 대시보드 조회 요청 - 유저ID : {}, 시작일 : {}, 종료일 : {}", userId, startDate, endDate);

        // 시작일이 종료일보다 늦으면 예외 발생
        if (startDate.isAfter(endDate))
            throw new IllegalArgumentException("startDate가 endDate보다 늦습니다.");

        return ResponseEntity.ok(service.getUserStatsOverview(userId, startDate, endDate));
    }

}

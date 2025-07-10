package com.mutedcritics.userStat.controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mutedcritics.userStat.service.UserStatService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
public class UserStatController {

    private final UserStatService service;

    @GetMapping("/user-dashboard")
    public ResponseEntity<?> getUserDashboard(
            @RequestParam String userId,
            @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate endDate) {
        log.info("유저 대시보드 조회 요청 - 유저ID : {}, 시작일 : {}, 종료일 : {}", userId, startDate, endDate);

        if (startDate.isAfter(endDate))
            throw new IllegalArgumentException("startDate가 endDate보다 늦습니다.");

        return ResponseEntity.ok(service.getDashboard(userId, startDate, endDate));
    }

}

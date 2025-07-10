package com.mutedcritics.userStat.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mutedcritics.dto.UserWinsRateDTO;
import com.mutedcritics.userStat.service.UserStatService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
public class UserStatController {

    private final UserStatService service;

    // 유저의 승리, 패배 횟수와 승률을 조회하는 메서드
    @GetMapping("/get-wins-rate")
    public ResponseEntity<List<UserWinsRateDTO>> getWinsRate(
            @RequestParam(required = false) String userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        log.info("유저 승률 조회 요청 - 시작일 {}, 종료일 : {}, 유저ID : {}", startDate, endDate, userId);
        try {
            List<UserWinsRateDTO> userWinsRate = service.getWinsRate(userId, startDate, endDate);
            return ResponseEntity.ok(userWinsRate);
        } catch (Exception e) {
            log.error("유저 승률 조회 실패 - 예외 발생 : {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

}

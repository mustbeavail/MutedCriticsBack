package com.mutedcritics.user.controller;

import com.mutedcritics.user.service.UserGroupService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user/group")
public class UserGroupController {
    private final UserGroupService service;

    // 유저 세분화(baseDate 는 필수 아님! 없으면 그냥 현재 날짜로 자동지정)
    @PostMapping("/classify")
    public ResponseEntity<Map<String, Object>> classifyUsers(
            @RequestParam(value = "baseDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate baseDate) {
        LocalDate date = baseDate != null ? baseDate : LocalDate.now();
        log.info("[유저 분류 시작] 기준일 : {}", date);
        service.classifyUsers(date);
        return ResponseEntity.ok(Map.of("msg", "유저 분류 완료 : " + date));
    }

}

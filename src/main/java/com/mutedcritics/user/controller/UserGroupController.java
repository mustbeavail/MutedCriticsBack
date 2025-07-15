package com.mutedcritics.user.controller;

import com.mutedcritics.user.service.UserGroupService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user/group")
public class UserGroupController {
    private final UserGroupService service;

    @PostMapping("/classify")
    public ResponseEntity<String> classifyUsers(
            @RequestParam(value = "baseDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate baseDate
    ) {
        LocalDate date = baseDate != null ? baseDate : LocalDate.now();
        log.info("[유저 분류 시작] 기준일 : {}", date);
        //service.classifyUsers(date);
        return ResponseEntity.ok("유저 분류 완료 (기준일: ) " + date + ")");
    }

}

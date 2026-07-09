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
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user/group")
public class UserGroupController {
    private final UserGroupService service;

    // 유저 세분화(baseDate 는 필수 아님! 없으면 그냥 현재 날짜로 자동지정)
    // [리뷰 환경 비활성] 우회 호출 시 전 유저가 휴면 처리/VIP 해제되어 리뷰 데이터가 훼손되는 취약점(B3).
    //  화면 호출 0건 + 스케줄러(classifyUsersDaily)도 이미 비활성이므로 엔드포인트 자체를 주석 처리.
    //  운영 전환 시 재개하려면: 관리자 인가 적용 후 활성화(재분류 로직 service.classifyUsers 는 그대로 유지).
    // @PostMapping("/classify")
    // public ResponseEntity<Map<String, Object>> classifyUsers(
    //         @RequestParam(value = "baseDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate baseDate) {
    //     LocalDate date = baseDate != null ? baseDate : LocalDate.now();
    //     log.info("[유저 분류 시작] 기준일 : {}", date);
    //     service.classifyUsers(date);
    //     return ResponseEntity.ok(Map.of("msg", "유저 분류 완료 : " + date));
    // }

}

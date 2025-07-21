package com.mutedcritics.user.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mutedcritics.dto.UserStatsDTO;
import com.mutedcritics.dto.UserStatsSeasonDTO;
import com.mutedcritics.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RestController
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    // 유저 상세 정보
    @GetMapping("/user/detail")
    public Map<String, Object> userDetail(@RequestParam String userId) {
        Map<String, Object> resp = new HashMap<>();

        resp = service.userDetail(userId);

        return resp;
    }

    // 유저 통계
    @GetMapping("/user/stats")
    public Map<String, Object> userStats(
            @RequestParam String userId) {

        Map<String, Object> resp = new HashMap<>();
        UserStatsDTO userStats = service.userStats(userId);

        resp.put("userStats", userStats);

        return resp;
    }

    // 유저 시즌별 통계
    @GetMapping("/user/stats/season")
    public Map<String, Object> userStatsSeason(
            @RequestParam String userId,
            @RequestParam int season) {
        Map<String, Object> resp = new HashMap<>();
        UserStatsSeasonDTO userStatsSeason = service.userStatsSeason(userId, season);

        resp.put("userStatsSeason", userStatsSeason);

        return resp;
    }

    // 유저 지출 상세내역
    @GetMapping("/user/spending")
    public Map<String, Object> userSpending(@RequestParam String userId) {
        Map<String, Object> resp = new HashMap<>();
        resp = service.userSpending(userId);
        return resp;
    }

    // 유저 리스트 불러오기
    @GetMapping("/user/list")
    public Map<String, Object> getUserList(
            @RequestParam(required = false) String searchType,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String userType,
            @RequestParam(defaultValue = "totalSpent") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("유저 리스트 조회 - searchType: {}, keyword: {}, region: {}, userType: {}, sortBy: {}, sortOrder: {}, page: {}, size: {}",
                searchType, keyword, region, userType, sortBy, sortOrder, page, size);

        return service.getUserList(searchType, keyword, region, userType, sortBy, sortOrder, page, size);
    }

}

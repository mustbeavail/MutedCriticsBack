package com.mutedcritics.userStat.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mutedcritics.dto.TierStatDTO;
import com.mutedcritics.dto.TierStatsRequestDTO;
import com.mutedcritics.userStat.service.UserTierStatsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 유저 분류별 티어 통계 컨트롤러
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class UserTierStatsController {

    private final UserTierStatsService service;

    @GetMapping("/user-tier-stats")
    public ResponseEntity<?> getTierStatistics(TierStatsRequestDTO params) {
        List<TierStatDTO> stats = service.getTierStatistics(params);
        return ResponseEntity.ok(stats);
    }

}
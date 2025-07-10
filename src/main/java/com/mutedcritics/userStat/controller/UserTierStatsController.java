package com.mutedcritics.userStat.controller;

import com.mutedcritics.dto.SeasonTierStatsDTO;
import com.mutedcritics.dto.TierStatsRequestDTO;
import com.mutedcritics.dto.TierStatsResponseDTO;
import com.mutedcritics.userStat.service.UserTierStatsService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 유저 티어 통계 관련 API 요청을 처리하는 컨트롤러 클래스.
 * 유저의 티어별 통계 및 분류 정보를 제공합니다.
 */
@RestController
@RequiredArgsConstructor // Lombok을 사용하여 final 필드에 대한 생성자를 자동으로 생성합니다 (의존성 주입).
public class UserTierStatsController {

    private final UserTierStatsService service; // 유저 티어 통계 서비스 주입

    /**
     * 유저 티어 통계 정보를 조회하는 GET 요청을 처리합니다.
     *
     * @param params 티어 통계 조회에 필요한 요청 파라미터 (예: 시즌 ID, 영웅 ID 등)
     * @return 티어 통계 응답 DTO를 담은 ResponseEntity
     */
    @GetMapping("/user-tier-stats")
    public ResponseEntity<TierStatsResponseDTO> getTierStatistics(TierStatsRequestDTO params) {
        // 서비스 계층에서 티어 통계 정보를 가져옵니다.
        TierStatsResponseDTO response = service.getTierStatistics(params);
        // 응답을 반환합니다.
        return ResponseEntity.ok(response);
    }

    /**
     * 시즌별 티어 통계 정보를 조회
     * 
     * @return 시즌별 티어 통계 DTO 리스트를 담고 있음.
     * 
     */
    @GetMapping("/season-tier-stats")
    public ResponseEntity<List<SeasonTierStatsDTO>> getSeasonTierStats(
            @RequestParam(required = false) Integer seasonIdx) {
        List<SeasonTierStatsDTO> response = service.getSeasonTierStats(seasonIdx);
        return ResponseEntity.ok(response);
    }
}
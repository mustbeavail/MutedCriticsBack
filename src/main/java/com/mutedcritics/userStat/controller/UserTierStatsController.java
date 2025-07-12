package com.mutedcritics.userstat.controller;

import com.mutedcritics.dto.SeasonTierStatsDTO;
import com.mutedcritics.dto.TierStatsRequestDTO;
import com.mutedcritics.dto.TierStatsResponseDTO;
import com.mutedcritics.dto.UserCategoryRequestDTO;
import com.mutedcritics.dto.UserCategoryResponseDTO;
import com.mutedcritics.userstat.service.UserTierStatsService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 유저 티어 통계 관련 API 요청을 처리하는 컨트롤러 클래스.
 * 유저의 티어별 통계 및 분류 정보를 제공합니다.
 */
@CrossOrigin
@RestController
@RequiredArgsConstructor // Lombok을 사용하여 final 필드에 대한 생성자를 자동으로 생성합니다 (의존성 주입).
public class UserTierStatsController {

    private final UserTierStatsService service; // 유저 티어 통계 서비스 주입

    /**
     * 유저 티어 통계 정보를 조회
     *
     * @param params 티어 통계 조회에 필요한 요청 파라미터(시즌idx, 성별, 지역, vip, 메인 영웅, 티어)
     * @return 티어 통계 응답 DTO를 담고 있음.
     */
    @GetMapping("/user-tier-stats")
    public ResponseEntity<TierStatsResponseDTO> getTierStatistics(TierStatsRequestDTO params) {
        TierStatsResponseDTO response = service.getTierStatistics(params);
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
            @RequestParam(required = false) int seasonIdx) {
        List<SeasonTierStatsDTO> response = service.getSeasonTierStats(seasonIdx);
        return ResponseEntity.ok(response);
    }

    /**
     * 유저 카테고리 정보를 조회합니다.
     *
     * 요청 예시:
     * GET /get-user-category?category=신규&limit=10&offset=0
     *
     * @param params 필터 및 페이징 파라미터
     *               - category: 신규, 복귀, 휴면, 정지, 이탈 위험군, vip, receive_yes,
     *               receive_no
     *               - limit: 반환할 최대 개수
     *               - offset: 시작 인덱스
     * @return 유저 카테고리 DTO 리스트
     */
    @GetMapping("/get-user-category")
    public ResponseEntity<UserCategoryResponseDTO> getUserCategory(UserCategoryRequestDTO params) {
        UserCategoryResponseDTO response = service.getUserCategory(params);
        return ResponseEntity.ok(response);
    }
}
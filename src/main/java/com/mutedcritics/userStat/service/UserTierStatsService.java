package com.mutedcritics.userstat.service;

import com.mutedcritics.dto.SeasonTierStatsDTO;
import com.mutedcritics.dto.TierStatDTO;
import com.mutedcritics.dto.TierStatsRequestDTO;
import com.mutedcritics.dto.TierStatsResponseDTO;
import com.mutedcritics.dto.UserClassificationDTO;
import com.mutedcritics.userstat.dao.UserTierStatsDAO;
import com.mutedcritics.dto.UserCategoryDTO;
import com.mutedcritics.dto.UserCategoryRequestDTO;
import com.mutedcritics.dto.UserCategoryResponseDTO;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserTierStatsService {

    private final UserTierStatsDAO dao;

    public TierStatsResponseDTO getTierStatistics(TierStatsRequestDTO params) {
        List<TierStatDTO> tierStats = dao.getTierStatistics(params);
        List<UserClassificationDTO> userList = dao.getUsersByClassification(params);
        return new TierStatsResponseDTO(tierStats, userList);
    }

    public List<SeasonTierStatsDTO> getSeasonTierStats(int seasonIdx) {
        List<SeasonTierStatsDTO> response = dao.getSeasonTierStats(seasonIdx);
        return response;
    }

    /**
     * 유저 카테고리 정보를 조회
     * 
     * @param params 유저 카테고리 조회에 필요한 요청 파라미터
     * @return 유저 카테고리 응답 DTO (데이터 + 페이징 정보)
     */
    public UserCategoryResponseDTO getUserCategory(UserCategoryRequestDTO params) {
        // 전체 개수 조회
        int totalCount = dao.getUserCategoryCount(params);

        // 데이터 조회
        List<UserCategoryDTO> data = dao.getUserCategory(params);

        // 페이징 계산
        int pageSize = params.getLimit() != null ? params.getLimit() : 10;
        int currentPage = params.getOffset() != null ? (params.getOffset() / pageSize) + 1 : 1;
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        List<Map<String, Object>> statsList = dao.getUserCategoryStats();

        return new UserCategoryResponseDTO(data, totalCount, totalPages, currentPage, pageSize, statsList);
    }
}
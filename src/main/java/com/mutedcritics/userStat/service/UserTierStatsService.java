package com.mutedcritics.userStat.service;

import com.mutedcritics.dto.SeasonTierStatsDTO;
import com.mutedcritics.dto.TierStatDTO;
import com.mutedcritics.dto.TierStatsRequestDTO;
import com.mutedcritics.dto.TierStatsResponseDTO;
import com.mutedcritics.dto.UserClassificationDTO;
import com.mutedcritics.userStat.dao.UserTierStatsDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
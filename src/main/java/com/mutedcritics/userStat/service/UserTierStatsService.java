package com.mutedcritics.userStat.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mutedcritics.dto.TierStatDTO;
import com.mutedcritics.dto.TierStatsRequestDTO;
import com.mutedcritics.userStat.dao.UserTierStatsDAO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 유저 분류별 티어 통계 서비스
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserTierStatsService {

    private final UserTierStatsDAO dao;

    public List<TierStatDTO> getTierStatistics(TierStatsRequestDTO params) {
        return dao.getTierStatistics(params);
    }

}
package com.mutedcritics.userStat.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mutedcritics.dto.TierStatDTO;
import com.mutedcritics.dto.TierStatsRequestDTO;

/**
 * 유저 분류별 티어 통계 DAO
 */
@Mapper
public interface UserTierStatsDAO {

    List<TierStatDTO> getTierStatistics(TierStatsRequestDTO params);

}
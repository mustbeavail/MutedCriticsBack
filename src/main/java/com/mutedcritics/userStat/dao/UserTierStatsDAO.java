package com.mutedcritics.userstat.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.mutedcritics.dto.SeasonTierStatsDTO;
import com.mutedcritics.dto.TierStatDTO;
import com.mutedcritics.dto.TierStatsRequestDTO;
import com.mutedcritics.dto.UserClassificationDTO;
import com.mutedcritics.dto.UserCategoryDTO;
import com.mutedcritics.dto.UserCategoryRequestDTO;

/**
 * 유저 분류별 티어 통계 DAO 인터페이스.
 * MyBatis 매퍼와 연결되어 유저의 티어 및 분류 관련 데이터를 조회합니다.
 */
@Mapper // MyBatis 매퍼 인터페이스임을 나타냅니다.
public interface UserTierStatsDAO {

    /**
     * 티어 통계 정보를 조회합니다.
     *
     * @param params 티어 통계 조회에 필요한 요청 파라미터
     * @return 티어별 통계 DTO 리스트
     */
    List<TierStatDTO> getTierStatistics(TierStatsRequestDTO params);

    /**
     * 유저 분류 정보를 조회합니다.
     *
     * @param params 유저 분류 조회에 필요한 요청 파라미터
     * @return 유저 분류 DTO 리스트
     */
    List<UserClassificationDTO> getUsersByClassification(TierStatsRequestDTO params);

    /**
     * 시즌별 티어 통계 정보를 조회합니다.
     * 
     * @return 시즌별 티어 통계 DTO 리스트
     */
    List<SeasonTierStatsDTO> getSeasonTierStats(int seasonIdx);

    /**
     * 유저 카테고리 정보를 조회합니다.
     * 
     * @param params 유저 카테고리 조회에 필요한 요청 파라미터
     * @return 유저 카테고리 DTO 리스트
     */
    List<UserCategoryDTO> getUserCategory(UserCategoryRequestDTO params);

    /**
     * 유저 카테고리 전체 개수를 조회합니다.
     * 
     * @param params 유저 카테고리 조회에 필요한 요청 파라미터
     * @return 유저 카테고리 전체 개수
     */
    int getUserCategoryCount(UserCategoryRequestDTO params);

    /**
     * 카테고리별 유저 수를 조회합니다.
     * 
     * @return 카테고리별 유저 수
     */
    List<Map<String, Object>> getUserCategoryStats();

}
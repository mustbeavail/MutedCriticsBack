package com.mutedcritics.ingame.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.mutedcritics.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IngameDAO {

    void insertDailyHeroStats(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    List<HeroItemCountDTO> getHeroItemCount(@Param("sortOrder") String sortOrder);

    List<Map<String, Object>> getHeroPlaytimePotgItemCount(@Param("sortOrder") String sortOrder, @Param("sortBy") String sortBy);

    // 영웅별 전체 플레이타임 조회
    List<HeroPlayTimeDTO> getHeroPlayTime(@Param("sortOrder") String sortOrder);

    // 모드별 전체 플레이타임 조회
    List<ModePlayTimeDTO> getModePlayTime(@Param("sortOrder") String sortOrder);

    // 영웅별 승률 조회 (기간별, 티어별 필터링)
    List<HeroWinRateResponseDTO> getHeroWinRate(HeroWinRateRequestDTO request);

    // 영웅별 최고의 플레이 비중 조회
    List<HeroPotgRateDTO> getHeroPotgRate(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("sortOrder") String sortOrder);

    // 영웅별 밴률/픽률 조회
    List<HeroBanPickRateDTO> getHeroBanPickRate(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("sortOrder") String sortOrder);

}

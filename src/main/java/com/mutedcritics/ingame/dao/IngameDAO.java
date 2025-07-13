package com.mutedcritics.ingame.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mutedcritics.dto.HeroBanPickRateDTO;
import com.mutedcritics.dto.HeroItemCountDTO;
import com.mutedcritics.dto.HeroPlayTimeDTO;
import com.mutedcritics.dto.HeroPotgRateDTO;
import com.mutedcritics.dto.HeroWinRateDTO;
import com.mutedcritics.dto.ModePlayTimeDTO;

@Mapper
public interface IngameDAO {

    void insertDailyHeroStats(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    List<HeroItemCountDTO> getHeroItemCount(@Param("sortOrder") String sortOrder);

    List<Map<String, Object>> getHeroPlaytimePotgItemCount(@Param("sortOrder") String sortOrder);

    // 영웅별 전체 플레이타임 조회
    List<HeroPlayTimeDTO> getHeroPlayTime(@Param("sortOrder") String sortOrder);

    // 모드별 전체 플레이타임 조회
    List<ModePlayTimeDTO> getModePlayTime(@Param("sortOrder") String sortOrder);

    // 영웅별 승률 조회 (기간별, 티어별 필터링)
    List<HeroWinRateDTO> getHeroWinRate(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("tierName") String tierName,
            @Param("sortOrder") String sortOrder);

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

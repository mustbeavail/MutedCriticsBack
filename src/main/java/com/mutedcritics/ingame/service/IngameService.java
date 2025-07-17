package com.mutedcritics.ingame.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.mutedcritics.dto.HeroBanPickRateDTO;
import com.mutedcritics.dto.HeroItemCountDTO;
import com.mutedcritics.dto.HeroPlayTimeDTO;
import com.mutedcritics.dto.HeroPotgRateDTO;
import com.mutedcritics.dto.HeroWinRateDTO;
import com.mutedcritics.dto.ModePlayTimeDTO;
import com.mutedcritics.ingame.dao.IngameDAO;

@Service
@Slf4j
@RequiredArgsConstructor
public class IngameService {

    private final IngameDAO dao;

    @Transactional
    public void insertDailyHeroStats(LocalDate startDate, LocalDate endDate) {
        log.info("일일 영웅 통계 업데이트 시작 : {} ~ {}", startDate, endDate);
        dao.insertDailyHeroStats(startDate, endDate);
        log.info("일일 영웅 통계 업데이트 완료 : {} ~ {}", startDate, endDate);
    }

    public List<HeroItemCountDTO> getHeroItemCount(String sortOrder) {
        return dao.getHeroItemCount(sortOrder);
    }

    public List<Map<String, Object>> getHeroPlaytimePotgItemCount(String sortOrder, String sortBy) {
        return dao.getHeroPlaytimePotgItemCount(sortOrder, sortBy);
    }

    /**
     * 영웅별 전체 플레이타임 조회
     */
    public List<HeroPlayTimeDTO> getHeroPlayTime(String sortOrder) {
        log.info("영웅별 전체 플레이타임 조회 - 정렬: {}", sortOrder);
        return dao.getHeroPlayTime(sortOrder);
    }

    /**
     * 모드별 전체 플레이타임 조회
     */
    public List<ModePlayTimeDTO> getModePlayTime(String sortOrder) {
        log.info("모드별 전체 플레이타임 조회 - 정렬: {}", sortOrder);
        return dao.getModePlayTime(sortOrder);
    }

    /**
     * 영웅별 승률 조회 (기간별, 티어별 필터링)
     */
    public List<HeroWinRateDTO> getHeroWinRate(LocalDate startDate, LocalDate endDate, String tierName,
            String sortOrder) {
        log.info("영웅별 승률 조회 - 기간: {} ~ {}, 티어: {}, 정렬: {}", startDate, endDate, tierName, sortOrder);
        return dao.getHeroWinRate(startDate, endDate, tierName, sortOrder);
    }

    /**
     * 영웅별 최고의 플레이 비중 조회
     */
    public List<HeroPotgRateDTO> getHeroPotgRate(LocalDate startDate, LocalDate endDate, String sortOrder) {
        log.info("영웅별 최고의 플레이 비중 조회 - 기간: {} ~ {}, 정렬: {}", startDate, endDate, sortOrder);
        return dao.getHeroPotgRate(startDate, endDate, sortOrder);
    }

    /**
     * 영웅별 밴률/픽률 조회
     */
    public List<HeroBanPickRateDTO> getHeroBanPickRate(LocalDate startDate, LocalDate endDate, String sortOrder) {
        log.info("영웅별 밴률/픽률 조회 - 기간: {} ~ {}, 정렬: {}", startDate, endDate, sortOrder);
        return dao.getHeroBanPickRate(startDate, endDate, sortOrder);
    }

}

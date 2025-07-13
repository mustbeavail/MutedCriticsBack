package com.mutedcritics.ingame.service;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.mutedcritics.dto.HeroItemCountDTO;
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

}

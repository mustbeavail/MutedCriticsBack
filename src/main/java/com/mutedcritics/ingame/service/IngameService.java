package com.mutedcritics.ingame.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.mutedcritics.ingame.dao.IngameDAO;

@Service
@RequiredArgsConstructor
public class IngameService {

    private final IngameDAO dao;

    /**
     * 전체 기간 승리/패배 횟수 insert
     * 
     * @param startDate
     * @param endDate
     * @return
     */
    public int insertWinLoseCount(LocalDate startDate, LocalDate endDate) {
        return dao.insertWinLoseCount(startDate, endDate);
    }

    /**
     * 전체 기간 픽 횟수 insert
     * 
     * @param startDate
     * @param endDate
     * @return
     */
    public int insertPickCount(LocalDate startDate, LocalDate endDate) {
        return dao.insertPickCount(startDate, endDate);
    }

}

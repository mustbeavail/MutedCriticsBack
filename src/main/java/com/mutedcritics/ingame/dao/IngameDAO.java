package com.mutedcritics.ingame.dao;

import java.time.LocalDate;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IngameDAO {

    /**
     * 전체 기간 승리/패배 횟수 insert
     * 
     * @param startDate
     * @param endDate
     * @return
     */
    int insertWinLoseCount(LocalDate startDate, LocalDate endDate);

    /**
     * 전체 기간 픽 횟수 insert
     * 
     * @param startDate
     * @param endDate
     * @return
     */
    int insertPickCount(LocalDate startDate, LocalDate endDate);

}

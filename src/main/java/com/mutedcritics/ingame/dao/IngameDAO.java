package com.mutedcritics.ingame.dao;

import java.time.LocalDate;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IngameDAO {

    void insertDailyHeroStats(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}

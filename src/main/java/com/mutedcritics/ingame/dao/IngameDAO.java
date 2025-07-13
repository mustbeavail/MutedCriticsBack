package com.mutedcritics.ingame.dao;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mutedcritics.dto.HeroItemCountDTO;

@Mapper
public interface IngameDAO {

    void insertDailyHeroStats(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    List<HeroItemCountDTO> getHeroItemCount(@Param("sortOrder") String sortOrder);

}

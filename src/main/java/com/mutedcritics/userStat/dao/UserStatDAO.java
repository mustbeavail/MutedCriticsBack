package com.mutedcritics.userStat.dao;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mutedcritics.dto.HeroModeTimeDTO;
import com.mutedcritics.dto.UserDashBoardDTO;

@Mapper
public interface UserStatDAO {

    UserDashBoardDTO selectUsersummary(String userId, LocalDate startDate, LocalDate endDate);

    List<HeroModeTimeDTO> selectHeroModeTime(String userId, LocalDate startDate, LocalDate endDate);

}

package com.mutedcritics.user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mutedcritics.dto.UserDTO;
import com.mutedcritics.dto.UserStatsDTO;
import com.mutedcritics.dto.UserStatsSeasonDTO;

@Mapper
public interface UserDAO {

    List<UserDTO> userDetail(String userId);

    UserStatsSeasonDTO userStatsSeason(String userId, int season);

    UserStatsDTO userStats(String userId);

}

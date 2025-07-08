package com.mutedcritics.user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mutedcritics.dto.UserDTO;
import com.mutedcritics.dto.UserTierDTO;

@Mapper
public interface UserDAO {

    List<UserDTO> userDetail(String userId);

    UserTierDTO userTierStats(String userId, int season);

}

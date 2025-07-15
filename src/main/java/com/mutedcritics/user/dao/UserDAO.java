package com.mutedcritics.user.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mutedcritics.dto.UserDTO;
import com.mutedcritics.dto.UserListDTO;
import com.mutedcritics.dto.UserStatsDTO;
import com.mutedcritics.dto.UserStatsSeasonDTO;

@Mapper
public interface UserDAO {

    List<UserDTO> userDetail(String userId);

    UserStatsSeasonDTO userStatsSeason(String userId, int season);

    UserStatsDTO userStats(String userId);

    List<Map<String, Object>> userSpending(String userId);

    // 유저 리스트 조회
    List<UserListDTO> getUserList(@Param("searchType") String searchType,
            @Param("keyword") String keyword,
            @Param("sortBy") String sortBy,
            @Param("sortOrder") String sortOrder,
            @Param("offset") int offset,
            @Param("size") int size);

    // 유저 리스트 전체 개수 조회
    int getUserListCount(@Param("searchType") String searchType,
            @Param("keyword") String keyword);

}

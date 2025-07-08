package com.mutedcritics.user.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.mutedcritics.dto.UserDTO;
import com.mutedcritics.dto.UserStatsDTO;
import com.mutedcritics.dto.UserTierDTO;
import com.mutedcritics.user.dao.UserDAO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserDAO dao;

    Map<String, Object> resp = null;

    // 유저 상세 정보
    public Map<String, Object> userDetail(String userId) {

        List<UserDTO> detail = dao.userDetail(userId);

        resp = new HashMap<>();

        resp.put("userDetail", detail);

        return resp;
    }

    // 유저 통계
    public UserStatsDTO userStats(String userId, int season) {

        UserStatsDTO resp = new UserStatsDTO();
        resp.setUser_id(userId);
        resp.setSeason(season);

        // 유저 티어 통계
        UserTierDTO tierStats = dao.userTierStats(userId, season);
        resp.setTier(tierStats);

        return resp;
    }

}

package com.mutedcritics.user.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mutedcritics.dto.UserListDTO;
import org.springframework.stereotype.Service;

import com.mutedcritics.dto.UserDTO;
import com.mutedcritics.dto.UserStatsDTO;
import com.mutedcritics.dto.UserStatsSeasonDTO;
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
    public UserStatsDTO userStats(String userId) {

        UserStatsDTO userStats = dao.userStats(userId);
        userStats.setUser_id(userId);

        return userStats;
    }

    // 유저 시즌별 통계
    public UserStatsSeasonDTO userStatsSeason(String userId, int season) {

        UserStatsSeasonDTO userStatsSeason = dao.userStatsSeason(userId, season);
        userStatsSeason.setUser_id(userId);
        userStatsSeason.setSeason(season);

        return userStatsSeason;
    }

    // 유저 지출 상세내역
    public Map<String, Object> userSpending(String userId) {
        List<Map<String, Object>> userSpendingList = dao.userSpending(userId);

        List<Map<String, Object>> itemList = new ArrayList<>();
        List<Map<String, Object>> bundleList = new ArrayList<>();
        Map<String, Object> resp = new HashMap<>();

        if (!userSpendingList.isEmpty()) {
            for (Map<String, Object> row : userSpendingList) {
                String itemCate = (String) row.get("item_cate");
                if ("item".equals(itemCate)) {
                    itemList.add(row);
                } else if ("bundle".equals(itemCate)) {
                    bundleList.add(row);
                }
            }
            resp.put("item", itemList.isEmpty() ? "구매이력 없음" : itemList);
            resp.put("bundle", bundleList.isEmpty() ? "구매이력 없음" : bundleList);
        } else {
            resp.put("item", "구매이력 없음");
            resp.put("bundle", "구매이력 없음");
        }
        return resp;
    }

    // 유저 리스트 불러오기
    public Map<String, Object> getUserList(String searchType, String keyword, String sortBy, String sortOrder, int page,
            int size) {
        resp = new HashMap<>();

        // 페이징 계산 (1-based page를 0-based offset으로 변환)
        int offset = (page - 1) * size;

        // 데이터 조회
        List<UserListDTO> userList = dao.getUserList(searchType, keyword, sortBy, sortOrder, offset, size);
        int totalCount = dao.getUserListCount(searchType, keyword);

        // 페이징 정보 계산
        int totalPages = (int) Math.ceil((double) totalCount / size);

        // 응답 데이터 구성
        resp.put("content", userList);
        resp.put("totalElements", totalCount);
        resp.put("totalPages", totalPages);
        resp.put("currentPage", page);
        resp.put("pageSize", size);
        resp.put("first", page == 1);
        resp.put("last", page == totalPages);
        resp.put("empty", userList.isEmpty());

        return resp;
    }
}

package com.mutedcritics.dto;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserCategoryResponseDTO {

    private List<UserCategoryDTO> userCategoryList;
    private int totalCount;
    private int totalPage;
    private int currentPage;
    private int pageSize;

    private List<Map<String, Object>> categoryStats; // 카테고리별 유저 수

}

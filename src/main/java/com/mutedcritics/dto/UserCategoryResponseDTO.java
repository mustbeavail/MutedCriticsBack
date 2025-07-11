package com.mutedcritics.dto;

import java.util.List;

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

}

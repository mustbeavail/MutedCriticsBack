package com.mutedcritics.dto;

import lombok.Data;

@Data
public class UserCategoryRequestDTO {

    private String category;
    private Integer limit;
    private Integer offset;

}
package com.mutedcritics.dto;

import lombok.Data;

@Data
public class UserCategoryRequestDTO {

    private String category;
    private int page = 1; // 기본 페이지 번호
    private int size = 10; // 기본 페이지 크기

    public int getOffset() {
        return (page - 1) * size;
    }

}
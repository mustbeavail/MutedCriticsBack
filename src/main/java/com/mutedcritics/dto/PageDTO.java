package com.mutedcritics.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PageDTO<T> {

    private int totalCount;
    private int page;
    private int size;
    private List<T> content;

}

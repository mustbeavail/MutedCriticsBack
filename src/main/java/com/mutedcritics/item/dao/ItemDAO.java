package com.mutedcritics.item.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ItemDAO {

    List<Map<String, Object>> dailyItemSalesInfo(LocalDate today);

}

package com.mutedcritics.item.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ItemDAO {

    // 일일 아이템 통계 조회
    List<Map<String, Object>> dailyItemSalesInfo(LocalDate today);

    // 일일 아이템 통계 저장
    int insertItemStats(Map<String, Object> resp);

    // 아이템 리스트 조회
    List<Map<String, Object>> itemList(
        @Param("startDate") String startDate,
        @Param("endDate") String endDate,
        @Param("align") String align,
        @Param("offset") int offset,
        @Param("search") String search);

    // 이벤트 아이템 리스트 조회
    List<Map<String, Object>> eventItemList(
        @Param("eventName") String eventName,
        @Param("align") String align);

    // 아이템 환불내역 조회 / 검색
    List<Map<String, Object>> refundList(String startDate, String endDate, String align, int offset, String search);

}

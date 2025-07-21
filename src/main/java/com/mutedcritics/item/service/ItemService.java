package com.mutedcritics.item.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.mutedcritics.item.dao.ItemDAO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {

    private final ItemDAO dao;

    // 일일 아이템 통계 조회
    public List<Map<String, Object>> dailyItemSalesInfo(LocalDate today) {

        return dao.dailyItemSalesInfo(today);
    }

    // 일일 아이템 통계 저장
    public boolean insertItemStats(Map<String, Object> resp) {

        int row = dao.insertItemStats(resp);
        
        return row > 0;
    }

    // 아이템 리스트 조회
    public List<Map<String, Object>> itemList(String startDate, String endDate, String align, Integer page, String search) {

        Integer offset = null;
        if (page != null) {
            offset = (page - 1) * 10;
        }

        return dao.itemList(startDate, endDate, align, offset, search);
    }

    // 이벤트 아이템 리스트 조회
    public List<Map<String, Object>> eventItemList(String eventName, String align, LocalDate today) {

        return dao.eventItemList(eventName, align, today);
    }

    // 아이템 환불내역 조회 / 검색
    public List<Map<String, Object>> refundList(String startDate, String endDate, String align, int page,
            String search) {

        int offset = (page - 1) * 10;

        return dao.refundList(startDate, endDate, align, offset, search);
    }

    // 아이템 환불내역 요약 조회
    public List<Map<String, Object>> refundSummary(String startDate, String endDate) {

        return dao.refundSummary(startDate, endDate);
    }
}

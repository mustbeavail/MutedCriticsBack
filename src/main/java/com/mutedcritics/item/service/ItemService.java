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
}

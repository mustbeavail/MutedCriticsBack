package com.mutedcritics.item.component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mutedcritics.item.service.ItemService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class ItemScheduler {

    private final ItemService service;

    // 일일 아이템 통계 조회 및 저장
    @Scheduled(cron = "0 0 1 * * *")
    public void itemStats() {
        log.info("일일 아이템 통계 시작");

        LocalDate today = LocalDate.now().minusDays(1);
        // 일일 아이템 통계 조회
        List<Map<String, Object>> salesInfoList = service.dailyItemSalesInfo(today);

        if (salesInfoList == null || salesInfoList.size() == 0) {
            log.warn("일일 아이템 통계 데이터가 없습니다.");
            throw new RuntimeException("일일 아이템 통계 데이터가 없습니다.");
        }

        // 일일 아이템 통계 저장
        for (Map<String, Object> salesInfo : salesInfoList) {
            Map<String, Object> resp = new HashMap<String, Object>();
            resp.put("stats_date", today);
            resp.put("item_idx", salesInfo.get("item_idx"));
            resp.put("daily_sales_revenue", salesInfo.get("daily_sales_revenue"));
            resp.put("daily_sales_count", salesInfo.get("daily_sales_count"));
            resp.put("daily_paying_users", salesInfo.get("daily_paying_users"));
            resp.put("daily_refund_amount", salesInfo.get("daily_refund_amount"));
            resp.put("daily_refund_count", salesInfo.get("daily_refund_count"));
            boolean success = service.insertItemStats(resp);
            if (success) {
                log.info("일일 아이템 통계 저장 성공: {}", salesInfo.get("item_idx"));
            } else {
                log.warn("일일 아이템 통계 저장 실패: {}", salesInfo.get("item_idx"));
            }
        }
    }
}

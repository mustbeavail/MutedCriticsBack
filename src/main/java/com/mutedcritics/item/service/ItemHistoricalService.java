package com.mutedcritics.item.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemHistoricalService {

    private final ItemService itemService;
    private static final int BATCH_SIZE = 60;

    public void processHistoricalStats(LocalDate startDate, LocalDate endDate) {
        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            for (int i = 0; i < BATCH_SIZE && !currentDate.isAfter(endDate); i++) {
                try {
                    Map<String, Object> params = new HashMap<>();
                    params.put("today", currentDate);

                    List<Map<String, Object>> salesInfoList = itemService.dailyItemSalesInfo(currentDate);
                    if (salesInfoList != null && !salesInfoList.isEmpty()) {
                        for (Map<String, Object> salesInfo : salesInfoList) {
                            params.put("stats_date", currentDate);
                            params.put("item_idx", salesInfo.get("item_idx"));
                            params.put("item_name", salesInfo.get("item_name"));
                            params.put("item_cate", salesInfo.get("item_cate"));
                            params.put("daily_sales_revenue", salesInfo.get("daily_sales_revenue"));
                            params.put("daily_sales_count", salesInfo.get("daily_sales_count"));
                            params.put("daily_paying_users", salesInfo.get("daily_paying_users"));
                            params.put("daily_refund_amount", salesInfo.get("daily_refund_amount"));
                            params.put("daily_refund_count", salesInfo.get("daily_refund_count"));
                            itemService.insertItemStats(params);
                            log.info("{}의 {}번 아이템 통계 처리 완료", currentDate, salesInfo.get("item_idx"));
                        }
                    }
                } catch (Exception e) {
                    log.error("{}의 통계 처리 실패: {}", currentDate, e.getMessage());
                }

                currentDate = currentDate.plusDays(1);
            }

            if (!currentDate.isAfter(endDate)) {
                log.info("{}일 처리 중 (총 {}일 중)", currentDate, ChronoUnit.DAYS.between(startDate, endDate) + 1);
            }

            // 잠시 쉬기 (서버 부하 방지)
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

}

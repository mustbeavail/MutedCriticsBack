package com.mutedcritics.notice.component;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mutedcritics.notice.service.NoticeService;
import com.mutedcritics.revenue.service.RevenueService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class NoticeScheduler {

    private final NoticeService notiService;
    private final RevenueService revenueService;
    LocalDate today = LocalDate.now();

    @Scheduled(cron = "0 0 0 1 * ?")
    public void sendMonthlyNotice() {
        log.info("월간 통계 알림 전송 시작");

        // 매출 급감 알림
        YearMonth YM = YearMonth.of(today.getYear(), today.getMonth()).minusMonths(1);
        LocalDate startDate = YM.atDay(1);
        LocalDate endDate = YM.atEndOfMonth();

        Map<String, Object> firstRevenuePeriod = revenueService.getRevenuePeriod(startDate.toString(), endDate.toString());
        List<Map<String, Object>> firstRevenueList = (List<Map<String, Object>>) firstRevenuePeriod.get("list");
        for (Map<String, Object> revenue : firstRevenueList) {
            int totalRevenue = 0;
            int dailyRevenue = (int) revenue.get("daily_revenue");
            totalRevenue += dailyRevenue;
        }

        



        

        // 아이템 매출 편중 심화 알림
        // vip 유저 접속/구매 감소 알림

    }

    @Scheduled(cron = "0 0 0 ? * MON")
    public void sendWeeklyNotice() {
        log.info("주간 통계 알림 전송 시작");

        // 영웅 승률 이상 상승 알림
        // 영웅 밴픽률 이상 상승 알림
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void sendDailyNotice() {
        log.info("일간 통계 알림 전송 시작");

        // 신고, 문의 급증 알림

    }

}

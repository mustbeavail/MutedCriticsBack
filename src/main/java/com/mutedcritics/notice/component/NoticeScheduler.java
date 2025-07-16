package com.mutedcritics.notice.component;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mutedcritics.notice.service.NoticeService;
import com.mutedcritics.activity.service.ActivityService;
import com.mutedcritics.item.service.ItemService;
import com.mutedcritics.revenue.service.RevenueService;
import com.mutedcritics.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class NoticeScheduler {

    private final NoticeService notiService;
    private final RevenueService revenueService;
    private final ItemService itemService;
    private final ActivityService activityService;
    private final UserService userService;

    LocalDate today = LocalDate.now();

    @Scheduled(cron = "0 0 0 1 * ?")
    public void sendMonthlyNotice() {
        log.info("월간 통계 알림 전송 시작");

        // 매출 급감 알림
        YearMonth YM = YearMonth.of(today.getYear(), today.getMonth()).minusMonths(1);
        YearMonth YM2 = YearMonth.of(today.getYear(), today.getMonth()).minusMonths(2);
        YearMonth YM3 = YearMonth.of(today.getYear(), today.getMonth()).minusMonths(3);
        LocalDate startDate = YM.atDay(1);
        LocalDate endDate = YM.atEndOfMonth();
        LocalDate startDate2 = YM2.atDay(1);
        LocalDate endDate2 = YM2.atEndOfMonth();
        LocalDate startDate3 = YM3.atDay(1);
        LocalDate endDate3 = YM3.atEndOfMonth();

        Map<String, Object> firstRevenuePeriod = revenueService.getRevenuePeriod(startDate.toString(), endDate.toString());
        if (firstRevenuePeriod.get("list") == null){
            throw new RuntimeException("첫 번째 기간의 매출 데이터를 찾을 수 없습니다.");
        }
        List<Map<String, Object>> firstRevenueList = (List<Map<String, Object>>) firstRevenuePeriod.get("list");

        Map<String, Object> secondRevenuePeriod = revenueService.getRevenuePeriod(startDate2.toString(), endDate2.toString());
        if (secondRevenuePeriod.get("list") == null){
            throw new RuntimeException("두 번째 기간의 매출 데이터를 찾을 수 없습니다.");
        }
        List<Map<String, Object>> secondRevenueList = (List<Map<String, Object>>) secondRevenuePeriod.get("list");

        Map<String, Object> thirdRevenuePeriod = revenueService.getRevenuePeriod(startDate3.toString(), endDate3.toString());
        if (thirdRevenuePeriod.get("list") == null){
            throw new RuntimeException("세 번째 기간의 매출 데이터를 찾을 수 없습니다.");
        }
        List<Map<String, Object>> thirdRevenueList = (List<Map<String, Object>>) thirdRevenuePeriod.get("list");

        long totalRevenue = 0;
        long totalRevenue2 = 0;
        long totalRevenue3 = 0;
        for (Map<String, Object> revenue : firstRevenueList) {
            long dailyRevenue = (long) revenue.get("daily_revenue");
            totalRevenue += dailyRevenue;
        }
        for (Map<String, Object> revenue : secondRevenueList) {
            long dailyRevenue = (long) revenue.get("daily_revenue");
            totalRevenue2 += dailyRevenue;
        }
        for (Map<String, Object> revenue : thirdRevenueList) {
            long dailyRevenue = (long) revenue.get("daily_revenue");
            totalRevenue3 += dailyRevenue;
        }
        boolean isDecrease = totalRevenue3 > totalRevenue2 && totalRevenue2 > totalRevenue;
        if (isDecrease){
            boolean success = notiService.saveRevenueDecreaseNotice(
                totalRevenue, totalRevenue2, totalRevenue3,
                YM, YM2, YM3);
            if (!success){
                throw new RuntimeException("월간 매출 감소 알림 전송 실패");
            }
        }else{
            log.info("월간 매출 안정적");
        }

        // 아이템 매출 편중 심화 알림
        // 모든 아이템 정보
        List<Map<String, Object>> itemList = itemService.itemList("1992-03-18", endDate.toString(), "desc", null, null);
        if (itemList == null || itemList.isEmpty()){
            throw new RuntimeException("아이템 리스트 조회 실패");
        }
        // 총 판매 금액
        long totalItemRevenue = 0;
        for (Map<String, Object> item : itemList) {
            long periodRevenue = (long) item.get("period_revenue");
            totalItemRevenue += periodRevenue;
        }
        if (totalItemRevenue <= 0L) {
            log.warn("오류 : 총매출 0 (집계기간 1992-03-18 ~ {})", endDate.toString());
            return;
        }
        // 아이템당 평균 판매 금액
        double avgItemRevenue = (double) totalItemRevenue / itemList.size();

        // 각 아이템이 총 판매 금액의 5%이상을 차지하는지 확인
        List<Map<String, Object>> candidateItemList = new ArrayList<>();
        for (Map<String, Object> item : itemList) {
            int itemIdx = (int) item.get("item_idx");
            String itemName = (String) item.get("item_name");
            long periodRevenue = (long) item.get("period_revenue");
            double itemRevenueRate = (periodRevenue * 100.0) / totalItemRevenue;
            if (itemRevenueRate >= 5.0){
                Map<String, Object> result = new HashMap<>();
                result.put("itemIdx", itemIdx);
                result.put("itemName", itemName);
                result.put("itemRevenueRate", itemRevenueRate);
                result.put("periodRevenue", periodRevenue);
                candidateItemList.add(result);
            }
        }

        // 총 판매금액의 5% 이상을 차지하는 후보군이 판매 금액이 평균 판매금액의 3배 이상인지 확인
        if (!candidateItemList.isEmpty()){

            List<Map<String, Object>> concentratedItems = new ArrayList<>();

            for (Map<String, Object> item : candidateItemList) {
                long periodRevenue = (long) item.get("periodRevenue");
                if (periodRevenue >= avgItemRevenue * 3){
                    double howManyTimes = (periodRevenue * 100.0) / avgItemRevenue;
                    long truncated = (long) howManyTimes;
                    Map<String, Object> result = new HashMap<>();
                    result.put("itemIdx", item.get("itemIdx"));
                    result.put("itemName", item.get("itemName"));
                    result.put("itemRevenueRate", item.get("itemRevenueRate"));
                    result.put("howManyTimes", truncated);
                    concentratedItems.add(result);
                }
            }
            if (!concentratedItems.isEmpty()){
                boolean success = notiService.saveConcentratedItemNotice(concentratedItems);
                if (!success){
                    throw new RuntimeException("월간 아이템 매출 편중 심화 알림 전송 실패");
                }
            }
        }else{
            log.info("월간 아이템 매출 편중 심화 사항 없음");
        }




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

package com.mutedcritics.activity.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.WeekFields;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.mutedcritics.activity.dao.ActivityDAO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityDAO dao;

    // 총 접속자 수
    public Map<String, Object> total_user() {
        return dao.total_user();
    }

    //일일 접속자 수 조회
    public int getDAU(LocalDate today) {
        return dao.DAU(today);
    }
    //일일 신규 유저 수 조회
    public int getNewUserCount(LocalDate today) {
        return dao.getNewUserCount(today);
    }
    // 일일 휴면 전환 유저 수 조회
    public int getDormantUserCount(LocalDate today) {
        return dao.getDormantUserCount(today);
    }
    // 일일 복귀 유저 수 조회
    public int getReturningUserCount(LocalDate today) {
        return dao.getReturningUserCount(today);
    }
    // 일일 탈퇴 유저 수 조회
    public int getWithdrawnUserCount(LocalDate today) {
        return dao.getWithdrawnUserCount(today);
    }
    
    // 일일 통계 저장
    public boolean insertDailyActivity(Map<String, Object> params) {
        int row = dao.insertDailyActivity(params);
        return row > 0;
    }

    // 기간별 일일 활성 이용자 수
    public Map<String, Object> periodDailyUser(String startDate, String endDate) {

        Map<String, Object> resp = new HashMap<>();

        List<Map<String, Object>> list = dao.periodDailyUser(startDate, endDate);

        resp.put("list", list);

        return resp;

    }

    // 기간별 주간 활성 이용자 수
    public Map<String, Object> periodWeeklyUser(
        int fromYear, int fromMonth, int fromWeek, int toYear, int toMonth, int toWeek) {

        Map<String, Object> resp = new HashMap<>();


        // 1) 일요일 시작, minimalDaysInFirstWeek=1
        WeekFields wf = WeekFields.of(DayOfWeek.SUNDAY, 1);

        // 2) 시작/끝 YearMonth
        YearMonth startYM = YearMonth.of(fromYear, fromMonth);
        YearMonth endYM   = YearMonth.of(toYear,   toMonth);

        // 3) 월 단위로 순회하며 주차별 날짜 계산
        Map<String, List<LocalDate>> weeklyDates = new LinkedHashMap<>();
        YearMonth ym = startYM;
        while (!ym.isAfter(endYM)) {
            LocalDate firstOfMonth = ym.atDay(1);
            LocalDate lastOfMonth  = ym.atEndOfMonth();
            int maxWeekInMonth = lastOfMonth.get(wf.weekOfMonth());

            int startWeek = ym.equals(startYM) ? fromWeek : 1;
            int endWeek   = ym.equals(endYM)   ? toWeek   : maxWeekInMonth;
            endWeek = Math.min(endWeek, maxWeekInMonth);

            for (int wk = startWeek; wk <= endWeek; wk++) {
                // 주차의 “일요일” 계산
                LocalDate weekStart = firstOfMonth
                .with(wf.weekOfMonth(), wk)
                .with(wf.dayOfWeek(), wf.getFirstDayOfWeek().getValue());
                // 주차의 “토요일” (일요일 + 6일)
                LocalDate weekEnd = weekStart.plusDays(6);

                // 날짜 시퀀스(endExclusive = weekEnd.plusDays(1)) 로 7일 모두 수집
                List<LocalDate> dates = weekStart
                    .datesUntil(weekEnd.plusDays(1))
                    .collect(Collectors.toList());

                String key = String.format("%d-%02d %d주차",
                    ym.getYear(), ym.getMonthValue(), wk);
                weeklyDates.put(key, dates);
            }
            ym = ym.plusMonths(1);
        }

        List<Map<String, Object>> periodWAU = new ArrayList<>();

        periodWAU = dao.periodWeeklyUser(weeklyDates);

        resp.put("periodWAU", periodWAU);

        return resp;
    }

    public Map<String, Object> periodMonthlyUser(int fromYear, int fromMonth, int toYear, int toMonth) {

        Map<String, Object> resp = new HashMap<>();

        YearMonth startYM = YearMonth.of(fromYear, fromMonth);
        YearMonth endYM   = YearMonth.of(toYear,   toMonth);

        Map<String, List<LocalDate>> monthlyDates = new LinkedHashMap<>();
        YearMonth ym = startYM;
        while (!ym.isAfter(endYM)) {
            LocalDate firstOfMonth = ym.atDay(1);
            LocalDate lastOfMonth  = ym.atEndOfMonth();

            List<LocalDate> dates = firstOfMonth.datesUntil(lastOfMonth.plusDays(1))
            .collect(Collectors.toList());

            String key = String.format("%d-%02d", ym.getYear(), ym.getMonthValue());
            monthlyDates.put(key, dates);
            ym = ym.plusMonths(1);
        }

        List<Map<String, Object>> periodMAU = new ArrayList<>();
        periodMAU = dao.periodMonthlyUser(monthlyDates);

        resp.put("periodMAU", periodMAU);

        return resp;
    }

}

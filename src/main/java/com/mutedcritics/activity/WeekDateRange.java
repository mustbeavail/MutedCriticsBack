package com.mutedcritics.activity;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class WeekDateRange {

    public static LocalDate[] getWeekRange(int year, int month, int weekOfMonth) {
        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);

        // 월요일 기준 offset 계산 (첫 월요일 찾기)
        int firstDayOfWeek = firstDayOfMonth.getDayOfWeek().getValue(); // 1=월요일, 7=일요일
        int offset = firstDayOfWeek <= DayOfWeek.MONDAY.getValue() ? 0 : 8 - firstDayOfWeek;
        LocalDate firstMonday = firstDayOfMonth.plusDays(offset);

        // 주차 시작일 = 첫 월요일 + (weekOfMonth - 1)주
        LocalDate startDate = firstMonday.plusWeeks(weekOfMonth - 1);
        LocalDate endDate = startDate.plusWeeks(1);

        return new LocalDate[] { startDate, endDate };
    }
    
}

package com.mutedcritics.activity;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class DateRange {

    /**
     * 특정 연도, 월, 주차에 해당하는 날짜 범위를 계산합니다.
     * 
     * @param year 연도
     * @param month 월 (1-12)
     * @param weekOfMonth 해당 월의 주차 (1부터 시작)
     * @return LocalDate 배열 [시작일, 종료일] 
     *         - 시작일: 해당 주의 월요일 (포함)
     *         - 종료일: 다음 주의 월요일 (미포함)
     *         - 실제 사용 시 종료일은 일요일(종료일 - 1일)까지 포함하는 것이 일반적입니다.
     */

    // 기간별 주간 활성 이용자 수
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

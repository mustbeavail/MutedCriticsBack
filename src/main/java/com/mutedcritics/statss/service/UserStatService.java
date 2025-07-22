package com.mutedcritics.statss.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.mutedcritics.dto.HeroModeTimeDTO;
import com.mutedcritics.dto.ModeTimeDTO;
import com.mutedcritics.dto.UserDashBoardDTO;
import com.mutedcritics.statss.dao.UserStatDAO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserStatService {

    private final UserStatDAO dao;

    /**
     * 유저 대시보드 정보를 조회하고 집계하는 메서드입니다.
     *
     * @param userId    조회할 유저 ID
     * @param startDate 조회 시작 날짜
     * @param endDate   조회 종료 날짜
     * @return 유저 대시보드 정보 (승률, 플레이타임, 주 역할군, 모드별 플레이타임 등)
     * @throws NoSuchElementException 유저 데이터를 찾을 수 없는 경우 발생
     */
    public Object getUserStatsOverview(String userId, LocalDate startDate, LocalDate endDate) {
        // 1. 유저 기본 통계 정보 조회 (승수, 패수, 승률, 총 플레이타임)
        UserDashBoardDTO summary = dao.selectUsersummary(userId, startDate, endDate);
        // 조회된 요약 정보가 없으면 예외 발생
        if (summary == null)
            throw new NoSuchElementException("이 유저의 데이터가 없습니다.");

        // 2. 영웅별, 모드별 플레이타임 상세 정보 조회
        List<HeroModeTimeDTO> heroModeTime = dao.selectHeroModeTime(userId, startDate, endDate);
        // 조회된 영웅/모드별 플레이타임 리스트를 summary DTO에 설정
        summary.setHeroTimes(heroModeTime);

        // 3. 주 역할군 계산 (가장 많이 플레이한 역할군 결정)
        String mainRole = heroModeTime.stream()
                // 역할군(role)별로 그룹화하고, 각 그룹의 플레이타임을 합산합니다.
                // 예: {"돌격": 100, "지원": 80, "공격": 50}
                .collect(Collectors.groupingBy(HeroModeTimeDTO::getRole,
                        Collectors.summingInt(HeroModeTimeDTO::getPlayTime)))
                // Map의 엔트리들을 스트림으로 변환합니다.
                .entrySet().stream()
                // 플레이타임(value)이 가장 큰 엔트리를 찾습니다.
                .max(Map.Entry.comparingByValue())
                // 찾은 엔트리에서 키(역할군 이름)만 추출합니다.
                .map(Map.Entry::getKey)
                // 만약 데이터가 없으면 "UNKNOWN"을 반환합니다.
                .orElse("UNKNOWN");
        // 계산된 주 역할군을 summary DTO에 설정
        summary.setMainRole(mainRole);

        // 4. 모드별 총 플레이타임
        Map<String, Integer> modePlayTimes = dao.selectModePlayTime(userId, startDate, endDate)
                .stream()
                .collect(Collectors.toMap(
                        ModeTimeDTO::getMatchMode,
                        ModeTimeDTO::getPlayTime));
        summary.setModePlayTimes(modePlayTimes);

        // 최종적으로 완성된 대시보드 정보를 반환합니다.
        return summary;
    }

}

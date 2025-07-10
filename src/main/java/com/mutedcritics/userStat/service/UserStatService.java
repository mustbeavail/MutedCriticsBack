package com.mutedcritics.userStat.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.mutedcritics.dto.HeroModeTimeDTO;
import com.mutedcritics.dto.UserDashBoardDTO;
import com.mutedcritics.userStat.dao.UserStatDAO;

import lombok.RequiredArgsConstructor;

/**
 * 유저 통계 관련 비즈니스 로직을 처리하는 서비스 클래스
 * 유저의 게임 플레이 데이터를 집계하고 대시보드 정보를 제공
 */
@Service
@RequiredArgsConstructor
public class UserStatService {

    private final UserStatDAO dao;

    /**
     * 유저 대시보드 정보를 조회하고 집계하는 메서드
     * 
     * @param userId    조회할 유저 ID
     * @param startDate 조회 시작 날짜
     * @param endDate   조회 종료 날짜
     * @return 유저 대시보드 정보 (승률, 플레이타임, 주 역할군, 모드별 플레이타임 등)
     * @throws NoSuchElementException 유저 데이터를 찾을 수 없는 경우
     */
    public Object getDashboard(String userId, LocalDate startDate, LocalDate endDate) {
        // 유저 기본 통계 정보 조회 (승수, 패수, 승률, 총 플레이타임)
        UserDashBoardDTO summary = dao.selectUsersummary(userId, startDate, endDate);
        if (summary == null)
            throw new NoSuchElementException("유저 대시보드 정보를 찾을 수 없습니다.");

        // 영웅별, 모드별 플레이타임 상세 정보 조회
        List<HeroModeTimeDTO> list = dao.selectHeroModeTime(userId, startDate, endDate);
        summary.setHeroTimes(list);

        // 주 역할군 계산 (가장 많이 플레이한 역할군 결정)
        // 1단계: list를 스트림으로 변환
        var mainRole = list.stream()
                // 2단계: 역할군(role)별로 그룹화하고, 각 그룹의 플레이타임을 합산
                // 예: {"돌격": 100, "지원": 80, "공격": 50}
                .collect(Collectors.groupingBy(HeroModeTimeDTO::getRole,
                        Collectors.summingLong(HeroModeTimeDTO::getPlayTime)))
                // 3단계: Map의 엔트리들을 스트림으로 변환
                .entrySet().stream()
                // 4단계: 플레이타임(value)이 가장 큰 엔트리를 찾기
                .max(Map.Entry.comparingByValue())
                // 5단계: 찾은 엔트리에서 키(역할군 이름)만 추출
                .map(Map.Entry::getKey)
                // 6단계: 만약 데이터가 없으면 "UNKNOWN" 반환
                .orElse("UNKNOWN");
        summary.setMainRole(mainRole);

        // 모드별 플레이타임 계산 (빠른대전, 경쟁전 등 각 모드별 총 플레이타임)
        // 1단계: list를 스트림으로 변환
        Map<String, Long> modePlayTimes = list.stream()
                // 2단계: 매치모드(matchMode)별로 그룹화하고, 각 그룹의 플레이타임을 합산
                // 예: {"빠른대전": 120, "경쟁전": 90}
                .collect(Collectors.groupingBy(HeroModeTimeDTO::getMatchMode,
                        Collectors.summingLong(HeroModeTimeDTO::getPlayTime)));
        summary.setModePlayTimes(modePlayTimes);

        return summary;
    }

}

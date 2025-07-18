package com.mutedcritics.userstat.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.mutedcritics.dto.HeroModeTimeDTO;
import com.mutedcritics.dto.UserDashBoardDTO;
import com.mutedcritics.userstat.dao.UserStatDAO;

import lombok.RequiredArgsConstructor;

/**
 * 유저 통계 관련 비즈니스 로직을 처리하는 서비스 클래스
 * 유저의 게임 플레이 데이터를 집계하고 대시보드 정보를 제공합니다.
 */
@Service // 이 클래스가 Spring 서비스 컴포넌트임을 나타냅니다.
@RequiredArgsConstructor // Lombok을 사용하여 final 필드에 대한 생성자를 자동으로 생성합니다 (의존성 주입).
public class UserStatService {

    private final UserStatDAO dao; // UserStatDAO 의존성 주입

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
        List<HeroModeTimeDTO> list = dao.selectHeroModeTime(userId, startDate, endDate);
        // 조회된 영웅/모드별 플레이타임 리스트를 summary DTO에 설정
        summary.setHeroTimes(list);

        // 3. 주 역할군 계산 (가장 많이 플레이한 역할군 결정)
        var mainRole = list.stream()
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

        // 4. 모드별 플레이타임 계산 (빠른대전, 경쟁전 등 각 모드별 총 플레이타임)
        Map<String, Integer> modePlayTimes = list.stream()
                // 매치모드(matchMode)별로 그룹화하고, 각 그룹의 플레이타임을 합산합니다.
                // 예: {"빠른대전": 120, "경쟁전": 90}
                .collect(Collectors.groupingBy(HeroModeTimeDTO::getMatchMode,
                        Collectors.summingInt(HeroModeTimeDTO::getPlayTime)));
        // 계산된 모드별 플레이타임을 summary DTO에 설정
        summary.setModePlayTimes(modePlayTimes);

        // 최종적으로 완성된 대시보드 정보를 반환합니다.
        return summary;
    }

}

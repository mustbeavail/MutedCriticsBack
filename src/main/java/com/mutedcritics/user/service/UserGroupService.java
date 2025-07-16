package com.mutedcritics.user.service;

import com.mutedcritics.user.dao.UserGroupDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UserGroupService {

    private final UserGroupDAO dao;

    /**
     * 기준일(baseDate) 기준으로 유저 분류 전체를 갱신한다.
     * baseDate를 전날로 주면 “전일 기준 스냅샷”이 된다.
     */
    @Transactional
    public void classifyUsers(LocalDate baseDate) {

        /* 0) vip 플래그 리셋 */
        dao.resetVipFlags();

        /* 0.1) vip 재계산 */
        dao.setVipFlags(baseDate);

        /* 1) 탈퇴 */
        dao.updateWithdrawnUsers(baseDate);

        /* 2) 휴면 */
        dao.updateDormantUsers(baseDate);

        /* 3) 복귀 (휴면 → 접속) */
        dao.updateReturningUsers(baseDate);

        /* 3.1) 복귀 2개월 경과 → 일반 */
        dao.updateReturningToNormal(baseDate);

        /* 4) 신규 */
        dao.updateNewUsers(baseDate);

        /* 5) 이탈 위험군 */
        dao.updateLeaverUsers(baseDate);

        /* 6) 나머지 전부 일반 */
        dao.updateGeneralUsers(baseDate);
    }

    /**
     * 매일 새벽 1시에 전일(어제) 기준으로 분류 스냅샷을 갱신합니다.
     * cron: 초 분 시 일 월 요일 (UTC+9 환경)
     */
    @Scheduled(cron = "0 0 1 * * *")
    public void classifyUsersDaily() {
        classifyUsers(LocalDate.now().minusDays(1));   // 어제 날짜
    }
}

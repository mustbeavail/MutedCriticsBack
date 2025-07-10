package com.mutedcritics.userStat.repository;

import java.time.LocalDate;
import java.util.List;

import com.mutedcritics.dto.UserWinsRateDTO;

public interface UserStatRepositoryCustom {

    // 유저의 승리, 패배 횟수와 승률을 조회
    List<UserWinsRateDTO> getUserWinsRate(String userId, LocalDate startDate, LocalDate endDate);

}

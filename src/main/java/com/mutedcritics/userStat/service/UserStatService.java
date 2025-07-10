package com.mutedcritics.userStat.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mutedcritics.dto.UserWinsRateDTO;
import com.mutedcritics.userStat.repository.UserStatRepositoryCustom;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserStatService {

    private final UserStatRepositoryCustom repository;

    // 유저의 승리, 패배 횟수와 승률을 조회
    public List<UserWinsRateDTO> getWinsRate(String userId, LocalDate startDate, LocalDate endDate) {
        return repository.getUserWinsRate(userId, startDate, endDate);
    }

}

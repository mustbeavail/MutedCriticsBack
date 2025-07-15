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

    @Transactional
    public void classifyUsers(LocalDate date) {
        dao.updateWithdrawnUsers(date);
        dao.updateDormantUsers(date);
        dao.updateLeaverUsers(date);
        dao.updateReturningUsers(date);
        dao.updateReturningToNormal(date);
        dao.updateNewUsers(date);
        dao.updateGeneralUsers(date);
    }

    @Scheduled(cron = "0 0 1 * * *")
    public void classifyUsersDaily() {
        classifyUsers(LocalDate.now().minusDays(1));
    }
}

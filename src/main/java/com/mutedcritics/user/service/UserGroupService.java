package com.mutedcritics.user.service;

import com.mutedcritics.user.dao.UserGroupDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UserGroupService {

    private final UserGroupDAO dao;

//    public void classifyUsers(LocalDate date) {
//        dao.updateWithdrawnUsers(date);
//        dao.updateWithdrawnUsers(date);
//        dao.updateWithdrawnUsers(date);
//        dao.updateWithdrawnUsers(date);
//        dao.updateWithdrawnUsers(date);
//        dao.updateWithdrawnUsers(date);
//        dao.updateWithdrawnUsers(date);
//
//    }
}

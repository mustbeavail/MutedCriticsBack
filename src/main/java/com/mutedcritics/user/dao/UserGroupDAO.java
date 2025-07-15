package com.mutedcritics.user.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;

@Mapper
public interface UserGroupDAO {
    void updateWithdrawnUsers(@Param("baseDate") LocalDate date);

    void updateDormantUsers(@Param("baseDate") LocalDate date);

    void updateLeaverUsers(@Param("baseDate") LocalDate date);

    void updateReturningUsers(@Param("baseDate") LocalDate date);

    void updateReturningToNormal(@Param("baseDate") LocalDate date);

    void updateNewUsers(@Param("baseDate") LocalDate date);

    void updateGeneralUsers(@Param("baseDate") LocalDate date);
}

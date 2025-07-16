package com.mutedcritics.user.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;

@Mapper
public interface UserGroupDAO {
    void resetVipFlags();

    void setVipFlags(@Param("baseDate") LocalDate baseDate);

    void updateWithdrawnUsers(@Param("baseDate") LocalDate baseDate);

    void updateDormantUsers(@Param("baseDate") LocalDate baseDate);

    void updateReturningUsers(@Param("baseDate") LocalDate baseDate);

    void updateReturningToNormal(@Param("baseDate") LocalDate baseDate);

    void updateNewUsers(@Param("baseDate") LocalDate baseDate);

    void updateLeaverUsers(@Param("baseDate") LocalDate baseDate);

    void updateGeneralUsers(@Param("baseDate") LocalDate baseDate);
}

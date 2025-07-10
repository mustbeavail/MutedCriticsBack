package com.mutedcritics.mail.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mutedcritics.entity.AutoSend;

public interface AutoSendRepository extends JpaRepository<AutoSend, Integer> {

    @Query("SELECT a FROM AutoSend a WHERE a.isActive = true AND a.nextSendDate <= :today")
    List<AutoSend> findScheduledMails(@Param("today") LocalDate today);

}

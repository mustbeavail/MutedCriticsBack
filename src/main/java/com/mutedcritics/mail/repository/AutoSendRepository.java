package com.mutedcritics.mail.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mutedcritics.entity.AutoSend;

public interface AutoSendRepository extends JpaRepository<AutoSend, Integer> {

    @Query("SELECT a FROM AutoSend a WHERE a.isActive = true AND a.nextSendDate <= :today")
    List<AutoSend> findScheduledMails(@Param("today") LocalDate today);

    @Query("SELECT a FROM AutoSend a WHERE a.mailSub LIKE %:search%")
    Page<AutoSend> findByMailSubContaining(@Param("search") String search, Pageable pageable);

    @Query("SELECT a FROM AutoSend a WHERE a.member.memberId LIKE %:search%")
    Page<AutoSend> findByMemberIdContaining(@Param("search") String search, Pageable pageable);

    @Query("SELECT a FROM AutoSend a WHERE a.recipient LIKE %:search%")
    Page<AutoSend> findByRecipientContaining(@Param("search") String search, Pageable pageable);

}

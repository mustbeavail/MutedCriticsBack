package com.mutedcritics.mail.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mutedcritics.entity.Mail;

public interface MailRepository extends JpaRepository<Mail, Integer> {

    @Query("SELECT m FROM Mail m WHERE m.mailSub LIKE %:search%")
    Page<Mail> findByMailSubContaining(@Param("search") String search, Pageable pageable);

    @Query("SELECT m FROM Mail m WHERE m.member.memberId LIKE %:search%")
    Page<Mail> findByMemberIdContaining(@Param("search") String search, Pageable pageable);

    @Query("SELECT m FROM Mail m WHERE m.recipient LIKE %:search%")
    Page<Mail> findByRecipientContaining(@Param("search") String search, Pageable pageable);

}

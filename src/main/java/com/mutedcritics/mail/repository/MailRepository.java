package com.mutedcritics.mail.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mutedcritics.entity.Mail;

public interface MailRepository extends JpaRepository<Mail, Integer> {

}

package com.mutedcritics.mail.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mutedcritics.entity.MailTemplate;

public interface MailTemplateRepository extends JpaRepository<MailTemplate, Integer> {

}

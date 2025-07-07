package com.mutedcritics.inquiry.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mutedcritics.entity.Inquiry;

public interface InquiryRepository extends JpaRepository<Inquiry, Integer>, InquiryRepositoryCustom {

}

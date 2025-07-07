package com.mutedcritics.inquiry.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mutedcritics.entity.Inquiry;

public interface InquiryRepositoryCustom {

    Page<Inquiry> findInquiriesWithConditions(String userId, String category, String status, String sortBy,
            String sortOrder, Pageable pageable);

}

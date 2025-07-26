package com.mutedcritics.inquiry.repository;

import com.mutedcritics.entity.Inquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InquiryRepositoryCustom {

        // 문의 리스트 조회
        Page<Inquiry> findInquiriesWithConditions(String userId, String category, String status, boolean isVip,
                        String sortBy, String sortOrder, Pageable pageable);

        // 신고 리스트 조회
        Page<Inquiry> findByReportsWithConditions(String userId, String status, String sortBy, String sortOrder,
                        Pageable pageable);

}

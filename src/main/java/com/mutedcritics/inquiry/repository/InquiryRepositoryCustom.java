package com.mutedcritics.inquiry.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mutedcritics.entity.Inquiry;

public interface InquiryRepositoryCustom {

        // 문의 리스트 조회
        Page<Inquiry> findInquiriesWithConditions(String userId, String category, String status, boolean isVip,
                        String sortBy, String sortOrder, Pageable pageable);

        // 신고 리스트 조회
        Page<Inquiry> findByReportsWithConditions(String userId, String status, String sortBy, String sortOrder,
                        Pageable pageable);

        // 답변이 없는 문의/신고 조회
        List<Inquiry> findUnansweredInquiries();

}

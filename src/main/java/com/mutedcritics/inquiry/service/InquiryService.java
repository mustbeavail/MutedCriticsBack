package com.mutedcritics.inquiry.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mutedcritics.dto.InquiryDto;
import com.mutedcritics.entity.Inquiry;
import com.mutedcritics.inquiry.repository.InquiryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryRepository repository;

    @Transactional(readOnly = true)
    public Page<InquiryDto> getInquiriesWithConditions(String userId, String category, String status, String sortBy,
            String sortOrder, Pageable pageable) {
        Page<Inquiry> inquiryPage = repository.findInquiriesWithConditions(userId, category, status, sortBy, sortOrder,
                pageable);

        List<Inquiry> inquiryList = inquiryPage.getContent();
        List<InquiryDto> dtoList = new ArrayList<>();

        for (Inquiry inquiry : inquiryList) {
            InquiryDto dto = InquiryDto.builder()
                    .inquiryIdx(inquiry.getInquiryIdx())
                    .category(inquiry.getCategory())
                    .title(inquiry.getTitle())
                    .content(inquiry.getContent())
                    .status(inquiry.getStatus())
                    .createdAt(inquiry.getCreatedAt())
                    .agentResYn(inquiry.isAgentResYn())
                    .userId(inquiry.getUser() != null ? inquiry.getUser().getUserId() : null)
                    .userNick(inquiry.getUser() != null ? inquiry.getUser().getUserNick() : null)
                    .reportedUserId(inquiry.getReportedUser() != null ? inquiry.getReportedUser().getUserId() : null)
                    .reportedUserNick(
                            inquiry.getReportedUser() != null ? inquiry.getReportedUser().getUserNick() : null)
                    .build();
            dtoList.add(dto);
        }

        return new PageImpl<>(dtoList, pageable, inquiryPage.getTotalElements());
    }

}

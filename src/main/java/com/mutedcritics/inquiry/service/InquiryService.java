package com.mutedcritics.inquiry.service;

import com.mutedcritics.dto.InquiryDTO;
import com.mutedcritics.dto.InquiryReportDetailDTO;
import com.mutedcritics.dto.ReportDTO;
import com.mutedcritics.dto.ResponseDTO;
import com.mutedcritics.entity.Inquiry;
import com.mutedcritics.entity.Response;
import com.mutedcritics.inquiry.repository.InquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryRepository repository;

    // 문의 리스트 조회
    @Transactional(readOnly = true)
    public Page<InquiryDTO> getInquiriesWithConditions(String userId, String category, String status, boolean isVip,
                                                       String sortBy,
                                                       String sortOrder, Pageable pageable) {
        Page<Inquiry> inquiryPage = repository.findInquiriesWithConditions(userId, category, status, isVip, sortBy,
                sortOrder, pageable);

        return inquiryPage.map(this::convertToInquiryDTO);
    }

    // 신고 리스트 조회
    @Transactional(readOnly = true)
    public Page<ReportDTO> getReportsWithConditions(String userId, String status, String sortBy, String sortOrder,
                                                    Pageable pageable) {
        Page<Inquiry> reportPage = repository.findByReportsWithConditions(userId, status, sortBy, sortOrder, pageable);

        return reportPage.map(this::convertToReportDTO);
    }

    // 문의 상세 조회
    @Transactional(readOnly = true)
    public InquiryReportDetailDTO getInquiryDetail(int inquiryIdx) {
        Inquiry inquiry = repository.findById(inquiryIdx)
                .orElseThrow(() -> new RuntimeException("문의 상세 조회 실패 : 문의 번호가 존재하지 않습니다."));

        return convertToDetailDTO(inquiry);
    }

    // 신고 상세 조회
    @Transactional(readOnly = true)
    public InquiryReportDetailDTO getReportDetail(int inquiryIdx) {
        Inquiry inquiry = repository.findById(inquiryIdx)
                .orElseThrow(() -> new RuntimeException("신고 상세 조회 실패 : 신고 번호가 존재하지 않습니다."));

        return convertToDetailDTO(inquiry);
    }

    // Inquiry 엔티티를 InquiryReportDetailDTO로 변환하는 메서드
    private InquiryReportDetailDTO convertToDetailDTO(Inquiry inquiry) {
        List<ResponseDTO> responseDTOs = new ArrayList<>();

        Response response = inquiry.getResponse();
        if (response != null) {
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .responseIdx(response.getResponseIdx())
                    .inquiryIdx(response.getInquiry().getInquiryIdx())
                    .agentId(response.getAgent() != null ? response.getAgent().getMemberId() : null)
                    .content(response.getContent())
                    .createdAt(response.getCreatedAt())
                    .build();
            responseDTOs.add(responseDTO);
        }

        return InquiryReportDetailDTO.builder()
                .inquiryIdx(inquiry.getInquiryIdx())
                .userId(inquiry.getUser() != null ? inquiry.getUser().getUserId() : null)
                .reportedUserId(inquiry.getReportedUser() != null ? inquiry.getReportedUser().getUserId() : null)
                .type(inquiry.getType())
                .category(inquiry.getCategory())
                .title(inquiry.getTitle())
                .content(inquiry.getContent())
                .status(inquiry.getStatus())
                .createdAt(inquiry.getCreatedAt())
                .responses(responseDTOs)
                .build();
    }

    // 엔티티 -> DTO 변환 메서드(문의 리스트)
    private InquiryDTO convertToInquiryDTO(Inquiry inquiry) {
        String userId = inquiry.getUser() != null ? inquiry.getUser().getUserId() : null;
        String reportedUserId = inquiry.getReportedUser() != null ? inquiry.getReportedUser().getUserId() : null;

        return InquiryDTO.builder()
                .inquiryIdx(inquiry.getInquiryIdx())
                .userId(userId)
                .reportedUserId(reportedUserId)
                .type(inquiry.getType())
                .category(inquiry.getCategory())
                .title(inquiry.getTitle())
                .content(inquiry.getContent())
                .status(inquiry.getStatus())
                .createdAt(inquiry.getCreatedAt())
                .build();
    }

    // 엔티티 -> DTO 변환 메서드(신고 리스트)
    private ReportDTO convertToReportDTO(Inquiry inquiry) {
        String userId = inquiry.getUser() != null ? inquiry.getUser().getUserId() : null;
        String reportedUserId = inquiry.getReportedUser() != null ? inquiry.getReportedUser().getUserId() : null;

        return ReportDTO.builder()
                .inquiryIdx(inquiry.getInquiryIdx())
                .userId(userId)
                .reportedUserId(reportedUserId)
                .type(inquiry.getType())
                .category(inquiry.getCategory())
                .title(inquiry.getTitle())
                .content(inquiry.getContent())
                .status(inquiry.getStatus())
                .createdAt(inquiry.getCreatedAt())
                .build();
    }

}

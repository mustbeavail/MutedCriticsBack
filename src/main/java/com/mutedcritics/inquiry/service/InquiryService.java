package com.mutedcritics.inquiry.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mutedcritics.dto.InquiryDTO;
import com.mutedcritics.dto.InquiryReportDetailDTO;
import com.mutedcritics.dto.ReportDTO;
import com.mutedcritics.dto.ResponseDTO;
import com.mutedcritics.entity.Inquiry;
import com.mutedcritics.entity.Response;
import com.mutedcritics.inquiry.repository.InquiryRepository;

import lombok.RequiredArgsConstructor;

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

		List<Inquiry> inquiryList = inquiryPage.getContent();
		List<InquiryDTO> dtoList = new ArrayList<>();

		for (Inquiry inquiry : inquiryList) {
			InquiryDTO dto = new InquiryDTO(inquiry);
			dtoList.add(dto);
		}

		return new PageImpl<>(dtoList, pageable, inquiryPage.getTotalElements());
	}

	// 신고 리스트 조회
	@Transactional(readOnly = true)
	public Page<ReportDTO> getReportsWithConditions(String userId, String status, String sortBy, String sortOrder,
			Pageable pageable) {
		Page<Inquiry> reportPage = repository.findByReportsWithConditions(userId, status, sortBy, sortOrder, pageable);

		List<Inquiry> reportList = reportPage.getContent();
		List<ReportDTO> dtoList = new ArrayList<>();

		for (Inquiry inquiry : reportList) {
			ReportDTO dto = new ReportDTO(inquiry);
			dtoList.add(dto);
		}

		return new PageImpl<>(dtoList, pageable, reportPage.getTotalElements());
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
				.responses(responseDTOs)
				.build();
	}

}

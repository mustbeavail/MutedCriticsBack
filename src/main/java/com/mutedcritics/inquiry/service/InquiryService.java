package com.mutedcritics.inquiry.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mutedcritics.dto.InquiryDTO;
import com.mutedcritics.dto.ReportDTO;
import com.mutedcritics.entity.Inquiry;
import com.mutedcritics.inquiry.repository.InquiryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InquiryService {

	private final InquiryRepository repository;

	@Transactional(readOnly = true)
	public Page<InquiryDTO> getInquiriesWithConditions(String userId, String category, String status, String sortBy,
			String sortOrder, Pageable pageable) {
		Page<Inquiry> inquiryPage = repository.findInquiriesWithConditions(userId, category, status, sortBy,
				sortOrder,
				pageable);

		List<Inquiry> inquiryList = inquiryPage.getContent();
		List<InquiryDTO> dtoList = new ArrayList<>();

		for (Inquiry inquiry : inquiryList) {
			InquiryDTO dto = new InquiryDTO(inquiry);
			dtoList.add(dto);
		}

		return new PageImpl<>(dtoList, pageable, inquiryPage.getTotalElements());
	}

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

}

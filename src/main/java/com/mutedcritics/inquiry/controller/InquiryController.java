package com.mutedcritics.inquiry.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mutedcritics.dto.InquiryDTO;
import com.mutedcritics.dto.InquiryReportDetailDTO;
import com.mutedcritics.dto.ReportDTO;
import com.mutedcritics.inquiry.service.InquiryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RestController
@Slf4j
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryService service;

    // 문의 리스트 조회(페이징, 정렬, 검색)
    // VIP 유저가 문의한 것만 조회할 수도 있어야...
    @GetMapping("/inquiry/list")
    public Page<InquiryDTO> getInquiryList(
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String status,
            @RequestParam(required = false, defaultValue = "false") boolean isVip,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("문의 리스트 불러오기 - userId: {}, category: {}, status: {}, isVip : {}, sortBy: {}, sortOrder: {}, page: {}, size: {}",
                userId, category, status, isVip, sortBy, sortOrder, page, size);

        Pageable pageable = PageRequest.of(page - 1, size);

        return service.getInquiriesWithConditions(userId, category, status, isVip, sortBy, sortOrder, pageable);
    }

    // 신고 리스트 조회(페이징, 정렬, 검색)
    @GetMapping("/report/list")
    public Page<ReportDTO> getReportList(
            @RequestParam(required = false) String userId,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("신고 리스트 불러오기 - userId: {}, sortBy: {}, sortOrder: {}, page: {}, size: {}",
                userId, sortBy, sortOrder, page, size);

        Pageable pageable = PageRequest.of(page - 1, size);

        return service.getReportsWithConditions(userId, status, sortBy, sortOrder, pageable);
    }

    // 문의 상세 조회
    @GetMapping("/inquiry/detail/{inquiryIdx}")
    public InquiryReportDetailDTO getInquiryDetail(@PathVariable int inquiryIdx) {

        log.info("문의 상세 조회 : {}", inquiryIdx);
        return service.getInquiryDetail(inquiryIdx);
    }

    // 신고 상세 조회
    @GetMapping("/report/detail/{inquiryIdx}")
    public InquiryReportDetailDTO getReportDetail(@PathVariable int inquiryIdx) {

        log.info("신고 상세 조회 : {}", inquiryIdx);
        return service.getReportDetail(inquiryIdx);
    }

}

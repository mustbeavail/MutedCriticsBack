package com.mutedcritics.inquiry.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mutedcritics.dto.InquiryDto;
import com.mutedcritics.inquiry.service.InquiryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin
@RestController
@Slf4j
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryService service;

    @GetMapping("/list/inquiry")
    public Page<InquiryDto> getInquiryList(
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("문의 리스트 불러오기 - userId: {}, category: {}, status: {}, sortBy: {}, sortOrder: {}, page: {}, size: {}",
                userId, category, status, sortBy, sortOrder, page, size);

        Pageable pageable = PageRequest.of(page, size);

        return service.getInquiriesWithConditions(userId, category, status, sortBy, sortOrder, pageable);
    }

}

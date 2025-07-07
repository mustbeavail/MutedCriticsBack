package com.mutedcritics.inquiry.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mutedcritics.entity.Inquiry;
import com.mutedcritics.inquiry.service.InquiryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin
@RestController
@Slf4j
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryService service;

    // 기본 문의/신고 리스트 조회
    @GetMapping("/getInquiryList")
    public List<Inquiry> getInquiryList() {
        log.info("문의/신고 리스트 불러오기");
        return service.getInquiryList();
    }

    // 페이징 처리된 문의/신고 리스트 조회
    @GetMapping("/getInquiryListWithPaging")
    public Page<Inquiry> getInquiryListWithPaging(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {

        log.info("페이징 처리된 문의/신고 리스트 불러오기: page={}, size={}, sortBy={}, direction={}", page, size, sortBy, direction);

        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        return service.getInquiryListWithPaging(pageable);
    }

    // 카테고리별 문의/신고 리스트 조회
    @GetMapping("/getInquiryListByCategory")
    public List<Inquiry> getInquiryListByCategory(@RequestParam String category) {
        log.info("카테고리별 문의/신고 리스트 불러오기: category={}", category);
        return service.getInquiryListByCategory(category);
    }

    // 상태별 문의/신고 리스트 조회
    @GetMapping("/getInquiryListByStatus")
    public List<Inquiry> getInquiryListByStatus(@RequestParam String status) {
        log.info("상태별 문의/신고 리스트 불러오기: status={}", status);
        return service.getInquiryListByStatus(status);
    }

    // 유저 ID로 문의/신고 리스트 조회
    @GetMapping("/getInquiryListByUserId")
    public List<Inquiry> getInquiryListByUserId(@RequestParam String userId) {
        log.info("유저 ID별 문의/신고 리스트 불러오기: userId={}", userId);
        return service.getInquiryListByUserId(userId);
    }

    // 피신고자 ID로 문의/신고 리스트 조회
    @GetMapping("/getInquiryListByReportedUserId")
    public List<Inquiry> getInquiryListByReportedUserId(@RequestParam String reportedId) {
        log.info("피신고자 ID별 문의/신고 리스트 불러오기: reportedId={}", reportedId);
        return service.getInquiryListByReportedUserId(reportedId);
    }

    // 날짜 범위로 문의/신고 리스트 조회
    @GetMapping("/getInquiryListByDateRange")
    public List<Inquiry> getInquiryListByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        log.info("날짜 범위별 문의/신고 리스트 불러오기: startDate={}, endDate={}", startDate, endDate);
        return service.getInquiryListByDateRange(startDate, endDate);
    }

    // 카테고리별 페이징 처리된 문의/신고 리스트 조회
    @GetMapping("/getInquiryListByCategoryWithPaging")
    public Page<Inquiry> getInquiryListByCategoryWithPaging(
            @RequestParam String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {

        log.info("카테고리별 페이징 처리된 문의/신고 리스트 불러오기: category={}, page={}, size={}, sortBy={}, direction={}",
                category, page, size, sortBy, direction);

        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        return service.getInquiryListByCategoryWithPaging(category, pageable);
    }

    // 상태별 페이징 처리된 문의/신고 리스트 조회
    @GetMapping("/getInquiryListByStatusWithPaging")
    public Page<Inquiry> getInquiryListByStatusWithPaging(
            @RequestParam String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {

        log.info("상태별 페이징 처리된 문의/신고 리스트 불러오기: status={}, page={}, size={}, sortBy={}, direction={}",
                status, page, size, sortBy, direction);

        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        return service.getInquiryListByStatusWithPaging(status, pageable);
    }

    // 유저 ID로 페이징 처리된 문의/신고 리스트 조회
    @GetMapping("/getInquiryListByUserIdWithPaging")
    public Page<Inquiry> getInquiryListByUserIdWithPaging(
            @RequestParam String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {

        log.info("유저 ID별 페이징 처리된 문의/신고 리스트 불러오기: userId={}, page={}, size={}, sortBy={}, direction={}",
                userId, page, size, sortBy, direction);

        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        return service.getInquiryListByUserIdWithPaging(userId, pageable);
    }
}

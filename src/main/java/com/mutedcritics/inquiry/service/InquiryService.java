package com.mutedcritics.inquiry.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mutedcritics.entity.Inquiry;
import com.mutedcritics.inquiry.repository.InquiryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryRepository repository;

    // 모든 문의/신고 리스트 조회
    public List<Inquiry> getInquiryList() {
        return repository.findAll();
    }

    // 페이징 처리된 문의/신고 리스트 조회
    public Page<Inquiry> getInquiryListWithPaging(Pageable pageable) {
        return repository.findAll(pageable);
    }

    // 카테고리별 문의/신고 리스트 조회
    public List<Inquiry> getInquiryListByCategory(String category) {
        return repository.findByCategory(category);
    }

    // 상태별 문의/신고 리스트 조회
    public List<Inquiry> getInquiryListByStatus(String status) {
        return repository.findByStatus(status);
    }

    // 유저 ID로 문의/신고 리스트 조회
    public List<Inquiry> getInquiryListByUserId(String userId) {
        return repository.findByUser_UserId(userId);
    }

    // 피신고자 ID로 문의/신고 리스트 조회
    public List<Inquiry> getInquiryListByReportedUserId(String reportedId) {
        return repository.findByReportedUser_UserId(reportedId);
    }

    // 날짜 범위로 문의/신고 리스트 조회
    public List<Inquiry> getInquiryListByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return repository.findByCreatedAtBetween(startDate, endDate);
    }

    // 카테고리별 페이징 처리된 문의/신고 리스트 조회
    public Page<Inquiry> getInquiryListByCategoryWithPaging(String category, Pageable pageable) {
        return repository.findByCategory(category, pageable);
    }

    // 상태별 페이징 처리된 문의/신고 리스트 조회
    public Page<Inquiry> getInquiryListByStatusWithPaging(String status, Pageable pageable) {
        return repository.findByStatus(status, pageable);
    }

    // 유저 ID로 페이징 처리된 문의/신고 리스트 조회
    public Page<Inquiry> getInquiryListByUserIdWithPaging(String userId, Pageable pageable) {
        return repository.findByUser_UserId(userId, pageable);
    }
}

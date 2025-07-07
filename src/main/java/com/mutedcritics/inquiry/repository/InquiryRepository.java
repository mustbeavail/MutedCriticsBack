package com.mutedcritics.inquiry.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mutedcritics.entity.Inquiry;

public interface InquiryRepository extends JpaRepository<Inquiry, Integer> {
    // 카테고리별 조회
    List<Inquiry> findByCategory(String category);

    // 상태별 조회
    List<Inquiry> findByStatus(String status);

    // 유저 ID로 검색
    List<Inquiry> findByUser_UserId(String userId);

    // 피신고자 ID로 검색
    List<Inquiry> findByReportedUser_UserId(String reportedId);

    // 날짜 범위로 검색
    List<Inquiry> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    // 페이징 처리
    Page<Inquiry> findAll(Pageable pageable);

    // 카테고리별 페이징 처리
    Page<Inquiry> findByCategory(String category, Pageable pageable);

    // 상태별 페이징 처리
    Page<Inquiry> findByStatus(String status, Pageable pageable);

    // 유저 ID로 페이징 처리
    Page<Inquiry> findByUser_UserId(String userId, Pageable pageable);
}

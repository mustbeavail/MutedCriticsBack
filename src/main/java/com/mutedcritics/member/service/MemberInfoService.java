package com.mutedcritics.member.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mutedcritics.dto.MemberInfoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.mutedcritics.entity.Member;
import com.mutedcritics.member.repository.MemberInfoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberInfoService {

    private final MemberInfoRepository repo;

    // 회원 리스트 조회 (페이징, 검색, 정렬, 상태 필터)
    public Map<String, Object> memberList(
            int page, int size, String keyword, String sortField, String sortDirection,
            String deptName, String position, Boolean acceptYn) {

        Map<String, Object> result = new HashMap<>();
        int pageNumber = Math.max(0, page - 1);

        // 상태 필터 처리
        Boolean acceptYn2 = null;
        if ("signUp".equals(acceptYn)) {
            acceptYn2 = true;
        } else if ("signUpWait".equals(acceptYn)) {
            acceptYn2 = false;
        }

        // 필터 적용 여부 판단
        boolean hasFilter = (keyword != null && !keyword.trim().isEmpty()) ||
                (deptName != null && !"전체".equals(deptName)) ||
                (position != null && !"전체".equals(position)) ||
                (acceptYn != null);

        long totalCount;
        List<Member> members;

        if (!hasFilter) {
            // 전체 조회
            totalCount = repo.countAllMembers();
            members = repo.findAllWithPagingAndSorting(
                    null, null, null, pageNumber, size, sortField, sortDirection, acceptYn);
        } else {
            // 필터 적용된 조회
            PageRequest pageable = PageRequest.of(pageNumber, size, repo.getSortByField(sortField, sortDirection));
            Page<Member> pageResult = repo.findByKeywordWithPagingAndSortingAndFilter(
                    keyword, deptName, position, acceptYn, pageable);
            totalCount = pageResult.getTotalElements();
            members = pageResult.getContent();
        }

        // 결과 조립
        int totalPages = (int) Math.ceil((double) totalCount / size);
        result.put("members", members);
        result.put("currentPage", page);
        result.put("totalItems", totalCount);
        result.put("totalPages", totalPages);

        return result;
    }

    // 관리자 권한 확인
    public boolean isAdmin(String member_id) {
        Member member = repo.findById(member_id).orElse(null);
        if (member == null) {
            return false;
        }
        return member.isAdminYn();
    }

    // 회원 정보 수정 (관리자만 가능)
    public boolean updateMember(String member_id, String email, String member_name, String office_phone,
                                String mobile_phone, String position, String dept_name, String requesterId) {
        // 요청자가 관리자인지 확인
        Member requester = repo.findById(requesterId).orElse(null);
        if (requester == null || !requester.isAdminYn()) {
            log.warn("회원 정보 수정 실패: 관리자 권한 없음 - requesterId={}", requesterId);
            return false;
        }

        // 수정할 회원 정보 조회
        Member member = repo.findById(member_id).orElse(null);
        if (member == null) {
            log.warn("회원 정보 수정 실패: 존재하지 않는 계정 - {}", member_id);
            return false;
        }

        // 정보 수정 (null이 아닌 값만 업데이트)
        if (email != null)
            member.setEmail(email);
        if (member_name != null)
            member.setMemberName(member_name);
        if (office_phone != null)
            member.setOfficePhone(office_phone);
        if (mobile_phone != null)
            member.setMobilePhone(mobile_phone);
        if (position != null)
            member.setPosition(position);
        if (dept_name != null)
            member.setDeptName(dept_name);

        repo.save(member);
        log.info("회원 정보 수정 성공: member_id={}, 요청자={}", member_id, requesterId);
        return true;
    }

    public MemberInfoDTO getMemberInfo(String memberId) {
        Member member = repo.findById(memberId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다." + memberId));

        return MemberInfoDTO.builder()
                .memberName(member.getMemberName())
                .memberId(member.getMemberId())
                .deptName(member.getDeptName())
                .positionName(member.getPosition())
                .email(member.getEmail())
                .officePhone(member.getOfficePhone())
                .mobilePhone(member.getMobilePhone())
                .build();
    }
}

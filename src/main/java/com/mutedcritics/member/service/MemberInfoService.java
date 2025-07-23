package com.mutedcritics.member.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mutedcritics.dto.MemberInfoDTO;
import com.mutedcritics.dto.MemberInfoRequestDTO;
import com.mutedcritics.dto.MemberWithdrawDTO;
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

    // 관리자가 회원을 탈퇴시키는 기능
    public Map<String, Object> withdrawMember(MemberWithdrawDTO request) {
        Map<String, Object> result = new HashMap<>();

        Member admin = repo.findById(request.getRequesterId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 관리자입니다."));

        // 관리자 권한 체크
        if (!admin.isAdminYn()) {
            throw new RuntimeException("관리자 권한이 없습니다.");
        }

        // 자기 자신은 탈퇴 불가능
        if (request.getRequesterId().equals(request.getMemberId())) {
            throw new RuntimeException("자기 자신은 탈퇴할 수 없습니다.");
        }

        // 탈퇴 대상 회원 조회
        Member member = repo.findById(request.getMemberId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        // 이미 탈퇴 처리 되었는지 확인
        if (member.getWithdrawDate() != null) {
            throw new RuntimeException("이미 탈퇴 처리된 회원입니다.");
        }

        // 탈퇴 처리
        member.setWithdrawDate(LocalDate.now());
        repo.save(member);

        result.put("success", true);
        result.put("msg", "정상적으로 탈퇴 처리되었습니다.");
        return result;
    }

    // 회원(본인) 정보 보기
    public MemberInfoDTO getMemberInfo(MemberInfoRequestDTO request) {
        // 본인이 본인 것 확인하는지 확인
        if (!request.getRequesterId().equals(request.getMemberId())) {
            throw new RuntimeException("본인만 조회할 수 있습니다.");
        }

        Member member = repo.findById(request.getMemberId())
                .orElseThrow(() -> new RuntimeException("회원 정보가 없습니다."));

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

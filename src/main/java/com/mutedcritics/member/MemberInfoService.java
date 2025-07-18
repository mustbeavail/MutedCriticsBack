package com.mutedcritics.member;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.mutedcritics.entity.Member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberInfoService {

    private final MemberInfoRepository repo;

    // 회원 리스트 조회 (페이징, 검색, 정렬)
    public Map<String, Object> memberList(
            int page, int size, String keyword, String sortField, String sortDirection,
            String dept_name, String position) {
        Map<String, Object> result = new HashMap<>();
        int pageNumber = Math.max(0, page - 1);

        long totalCount;
        List<Member> members;

        if ((keyword == null || keyword.trim().isEmpty()) &&
                (dept_name == null || dept_name.equals("전체")) &&
                (position == null || position.equals("전체"))) {
            // 필터가 없으면 전체 회원 수 조회
            totalCount = repo.countAllMembers();
            members = repo.findAllWithPagingAndSorting(dept_name, position, keyword, pageNumber, size, sortField,
                    sortDirection);
        } else {
            // 필터가 있을 경우 필터에 맞는 회원 수 조회
            totalCount = repo.countMembersByKeywordAndFilter(keyword, dept_name, position);
            members = repo.findByKeywordWithPagingAndSorting(keyword, pageNumber, size, sortField, sortDirection,
                    dept_name, position);
        }

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

    // 가입 대기 인원 리스트
    public List<Member> waitingList() {
        return repo.findAllByAcceptYn(false);
    }

}

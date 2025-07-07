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

    private final MemberInfoRepo repo;

    // 회원 리스트 조회 (페이징, 검색, 정렬)
    public Map<String, Object> memberList(int page, int size, String keyword, String sortField, String sortDirection) {
        try {
            // 결과를 담을 맵
            Map<String, Object> result = new HashMap<>();
            
            // 페이지 번호는 0부터 시작 (1페이지 -> 0)
            int pageNumber = page - 1;
            if (pageNumber < 0) pageNumber = 0;
            
            // 검색어가 없으면 전체 회원 수 조회
            long totalCount;
            List<Member> members;
            
            if (keyword == null || keyword.trim().isEmpty()) {
                // 검색어 없음 - 전체 회원 리스트
                totalCount = repo.countAllMembers();
                members = repo.findAllWithPagingAndSorting(pageNumber, size, sortField, sortDirection);
            } else {
                // 검색어 있음 - 필터링된 회원 리스트
                totalCount = repo.countMembersByKeyword(keyword);
                members = repo.findByKeywordWithPagingAndSorting(keyword, pageNumber, size, sortField, sortDirection);
            }
            
            int totalPages = (int) Math.ceil((double) totalCount / 10);
            
            result.put("members", members);
            result.put("currentPage", page);
            result.put("totalItems", totalCount);
            result.put("totalPages", totalPages);
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("error", e.getMessage());
            return errorResult;
        }
    }

}

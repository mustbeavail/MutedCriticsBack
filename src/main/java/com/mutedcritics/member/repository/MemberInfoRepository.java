package com.mutedcritics.member.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mutedcritics.entity.Member;

public interface MemberInfoRepository extends JpaRepository<Member, String> {

    // 전체 회원 수 조회
    @Query("SELECT COUNT(m) FROM Member m")
    long countAllMembers();

    // 키워드로 회원 수 조회 (아이디, 이름으로 검색)
    @Query("SELECT COUNT(m) FROM Member m WHERE " +
            "(:keyword IS NULL OR m.memberId LIKE %:keyword% OR m.memberName LIKE %:keyword%) AND " +
            "(:deptName IS NULL OR :deptName = '전체' OR m.deptName = :deptName) AND " +
            "(:position IS NULL OR :position = '전체' OR m.position = :position) AND " +
            "(:acceptYn IS NULL OR m.acceptYn = :acceptYn)")
    long countMembersByKeywordAndFilter(@Param("keyword") String keyword,
            @Param("deptName") String deptName,
            @Param("position") String position,
            @Param("acceptYn") Boolean acceptYn);

    // 페이징과 정렬을 적용한 전체 회원 조회
    default List<Member> findAllWithPagingAndSorting(String dept_name, String position, String keyword, int page,
            int size, String sortField, String sortDirection, Boolean acceptYn) {
        Sort sort = getSortByField(sortField, sortDirection);
        // 페이지 크기를 10으로 고정
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Member> result = findByKeywordWithPagingAndSortingAndFilter(keyword, dept_name, position, acceptYn,
                pageable);
        return result.getContent();
    }

    // 정렬 필드와 방향 설정
    default Sort getSortByField(String sortField, String sortDirection) {
        if (sortField == null || sortField.isEmpty()) {
            sortField = "memberId"; // 기본 정렬 필드
        }

        Sort sort;
        if ("desc".equalsIgnoreCase(sortDirection)) {
            sort = Sort.by(sortField).descending();
        } else {
            sort = Sort.by(sortField).ascending();
        }

        return sort;
    }

    @Query("SELECT m FROM Member m WHERE " +
            "(:keyword IS NULL OR m.memberId LIKE %:keyword% OR m.memberName LIKE %:keyword%) AND " +
            "(:deptName IS NULL OR :deptName = '전체' OR m.deptName = :deptName) AND " +
            "(:position IS NULL OR :position = '전체' OR m.position = :position) AND " +
            "(:acceptYn IS NULL OR m.acceptYn = :acceptYn)")
    Page<Member> findByKeywordWithPagingAndSortingAndFilter(
            @Param("keyword") String keyword,
            @Param("deptName") String deptName,
            @Param("position") String position,
            @Param("acceptYn") Boolean acceptYn,
            Pageable pageable);
}

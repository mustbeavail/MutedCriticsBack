package com.mutedcritics.member.controller;

import com.mutedcritics.dto.MemberInfoDTO;
import com.mutedcritics.member.service.MemberInfoService;
import com.mutedcritics.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


@RestController
@Slf4j
@RequiredArgsConstructor
public class MemberInfoController {

    private final MemberInfoService service;

    // 회원 리스트 보기 (정렬, 검색 기능 필요)
    @GetMapping("/memberInfo/list/{page}")
    public Map<String, Object> memberList(
            @PathVariable(required = false) Integer page,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "memberId") String sortField,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @RequestParam(required = false) String dept_name,
            @RequestParam(required = false) String position,
            @RequestParam(required = false) Boolean acceptYn) {

        int pageNumber = page;
        int size = 10;

        log.info("회원 리스트 요청: page={}, keyword={}, sortField={}, sortDirection={}, dept_name={}, position={}, acceptYn={}",
                pageNumber, keyword, sortField, sortDirection, dept_name, position, acceptYn);
        return service.memberList(pageNumber, size, keyword, sortField, sortDirection, dept_name, position, acceptYn);
    }

    // 회원 정보 수정 (관리자만 가능)
    @PostMapping("/memberInfo/update/{member_id}")
    public Map<String, Object> updateMember(@PathVariable String member_id, @RequestBody Map<String, String> params, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();

        // 토큰 검증
        String token = request.getHeader("authorization");
        if (token == null || token.isEmpty()) {
            result.put("success", false);
            result.put("message", "인증 토큰이 필요합니다.");
            return result;
        }
        Map<String, Object> payload = JwtUtil.readToken(token);
        String requesterId = (String) payload.get("member_id");
        if (requesterId == null || requesterId.isEmpty()) {
            result.put("success", false);
            result.put("message", "유효하지 않은 토큰입니다.");
            return result;
        }

        // 관리자 권한 확인
        if (!service.isAdmin(requesterId)) {
            log.warn("회원 정보 수정 실패: 요청자({})가 관리자가 아닙니다", requesterId);
            result.put("success", false);
            result.put("message", "관리자 권한이 필요합니다.");
            return result;
        }

        log.info("회원 정보 수정 요청 파라미터: {}", params);
        String email = params.get("email");
        String member_name = params.get("member_name");
        String office_phone = params.get("office_phone");
        String mobile_phone = params.get("mobile_phone");
        String position = params.get("position");
        String dept_name = params.get("dept_name");

        boolean success = service.updateMember(member_id, email, member_name, office_phone, mobile_phone, position, dept_name, requesterId);
        result.put("success", success);

        if (success) {
            result.put("message", "회원 정보가 수정되었습니다.");
        } else {
            result.put("message", "회원 정보 수정에 실패했습니다.");
        }
        return result;
    }

    // 관리자가 회원을 탈퇴시키는 기능
    @PostMapping("/memberInfo/withdraw/{memberId}")
    public ResponseEntity<?> withdrawMember(@PathVariable String memberId) {
        return null;
    }


    // 회원(본인) 정보 보기
    @GetMapping("/memberInfo/{memberId}")
    public ResponseEntity<?> getMemberInfo(@PathVariable String memberId) {
        log.info("memberId: {}", memberId);
        MemberInfoDTO memberInfo = service.getMemberInfo(memberId);
        return ResponseEntity.ok(memberInfo);
    }

}

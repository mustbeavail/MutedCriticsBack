package com.mutedcritics.member;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mutedcritics.utils.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin
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
            HttpServletRequest request) {
        
        String token = request.getHeader("authorization");
        if (token == null || token.isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            return errorResponse;
        }
        
        try {
            Map<String, Object> payload = JwtUtil.readToken(token);
            String memberId = (String) payload.get("member_id");
            
            if (memberId == null || memberId.isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                return errorResponse;
            }
            
            int pageNumber = page;
            int size = 10;
            
            log.info("회원 리스트 요청: page={}, keyword={}, sortField={}, sortDirection={}", 
                    pageNumber, keyword, sortField, sortDirection);
            return service.memberList(pageNumber, size, keyword, sortField, sortDirection); // 회원 리스트 조회
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            return errorResponse;
        }
    }

    // 회원 정보 수정

}

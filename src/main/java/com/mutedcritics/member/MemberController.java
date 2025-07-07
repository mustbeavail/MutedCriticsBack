package com.mutedcritics.member;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mutedcritics.entity.Member;
import com.mutedcritics.utils.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin
@RestController
@Slf4j
@RequiredArgsConstructor
public class MemberController {

    private final MemberService service;

    // 로그인
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> params) {
        String member_id = params.get("member_id");
        String member_pw = params.get("member_pw");
        
        log.info("로그인 요청: {}", member_id);
        Map<String, Object> result = new HashMap<String, Object>();
        boolean success = service.login(member_id, member_pw);
        result.put("success", success);
        
        // 로그인 성공 시 JWT 토큰 생성
        if (success) {
            // JWT 토큰에 저장할 정보
            Map<String, Object> tokenData = new HashMap<>();
            tokenData.put("member_id", member_id);
            
            // JWT 키가 없으면 생성
            if (JwtUtil.getPri_key() == null) {
                JwtUtil.setPri_key();
            }
            
            // 토큰 생성
            String token = JwtUtil.getToken(tokenData);
            
            // 응답에 토큰 추가
            result.put("token", token);
        }
        
        return result;
    }

    // 회원가입
    @PostMapping("/join")
    public Map<String, Object> join(@RequestBody Member params) {
        log.info("회원가입 요청: {}", params.getMemberId());
        Map<String, Object> result = new HashMap<String, Object>();
        
        boolean success = service.join(params);
        result.put("success", success);
        return result;
    }

    // 이메일 인증 코드 발송
    @PostMapping("/find_pw/send_code")
    public Map<String, Object> emailcode(@RequestBody Map<String, String> params) {
        log.info("비밀번호 찾기 요청 파라미터: {}", params);
        
        String member_id = params.get("memberId");
        String email = params.get("email");

        Map<String, Object> result = new HashMap<String, Object>();
        
        boolean success = service.emailcode(member_id, email);
        result.put("success", success);
        
        return result;
    }
    
    // 인증 코드 검증
    @PostMapping("/find_pw/verify_code")
    public Map<String, Object> verifyCode(@RequestBody Map<String, String> params) {
        log.info("인증 코드 검증 요청 파라미터: {}", params);
        
        String member_id = params.get("memberId");
        String authCode = params.get("authCode");

        Map<String, Object> result = new HashMap<String, Object>();
        
        boolean success = service.verifyAuthCode(member_id, authCode);
        result.put("success", success);
        
        return result;
    }
    
    // 비밀번호 변경
    @PostMapping("/find_pw/change_password")
    public Map<String, Object> changePassword(@RequestBody Map<String, String> params) {
        log.info("비밀번호 변경 요청 파라미터: {}", params);
        
        String member_id = params.get("memberId");
        String new_password = params.get("member_pw");
        
        Map<String, Object> result = new HashMap<String, Object>();
        
        boolean success = service.changePassword(member_id, new_password);
        result.put("success", success);
        
        return result;
    }
    
}

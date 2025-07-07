package com.mutedcritics.member;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mutedcritics.entity.Member;
import com.mutedcritics.utils.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import javax.servlet.http.HttpServletRequest;

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
            Map<String, Object> tokenData = new HashMap<>();
            tokenData.put("member_id", member_id); // JWT 토큰에 저장할 정보
            if (JwtUtil.getPri_key() == null) {
                JwtUtil.setPri_key();
            } // JWT 키가 없으면 생성
            String token = JwtUtil.getToken(tokenData); // 토큰 생성
            result.put("token", token); // 응답에 토큰 추가
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

    // 관리자 권한 부여
    @GetMapping("/grant_admin/{member_id}")
    public Map<String, Object> grant_admin(@PathVariable String member_id, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();

        // 토큰 검증
        String token = request.getHeader("authorization");
        if (token == null || token.isEmpty()) {
            result.put("success", false);
            return result;
        }

        Map<String, Object> payload = JwtUtil.readToken(token);
        String requesterId = (String) payload.get("member_id");

        if (requesterId == null || requesterId.isEmpty()) {
            result.put("success", false);
            return result;
        }

        // 관리자 권한 확인 - 요청자가 관리자인지 체크
        if (!service.isAdmin(requesterId)) {
            log.info("관리자 권한 부여 실패: 요청자({})가 관리자가 아닙니다", requesterId);
            result.put("success", false);
            return result;
        }

        // 관리자 권한 부여
        boolean success = service.grant_admin(member_id);
        result.put("success", success);
        return result;

    }

}

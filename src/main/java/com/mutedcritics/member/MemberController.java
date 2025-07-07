package com.mutedcritics.member;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mutedcritics.entity.Member;

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
    public Map<String, Object> login(String member_id, String member_pw) {
        Map<String, Object> result = new HashMap<String, Object>();
        boolean success = service.login(member_id, member_pw);
        result.put("success", success);
        return result;
    }

    // 회원가입
    @PostMapping("/join")
    public Map<String, Object> join(Member params) {
        Map<String, Object> result = new HashMap<String, Object>();
        boolean success = service.join(params);
        result.put("success", success);
        return result;
    }
}

package com.mutedcritics.member;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mutedcritics.entity.Member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepo repo;
    private final PasswordEncoder encoder;

    // 로그인
    public boolean login(String member_id, String member_pw) {
        Member member = repo.findById(member_id).orElse(null);
        if(member == null) {
            return false;
        }
        String hash = member.getMemberPw();
        return encoder.matches(member_pw, hash);
    }

    // 회원가입
    public boolean join(Member params) {
        // null 체크 (암호화 오류 방지용)
        if (params.getMemberPw() == null) {
            return false;
        }
        
        // 비밀번호 암호화
        String hash = encoder.encode(params.getMemberPw());
        params.setMemberPw(hash);
        Member member = repo.save(params);
        return member != null;
    }

}

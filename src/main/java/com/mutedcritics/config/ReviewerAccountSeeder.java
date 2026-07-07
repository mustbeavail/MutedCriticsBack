package com.mutedcritics.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.mutedcritics.entity.Member;
import com.mutedcritics.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 리뷰용 열람 전용 계정(reviewer1~6) 멱등 시더.
 * 부팅 시 계정이 없으면 생성한다(reviewer_yn=true, admin_yn=true, accept_yn=true).
 * 비밀번호는 BCrypt로 인코딩("pass"). 이미 존재하면 건너뛴다.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ReviewerAccountSeeder implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    private static final int REVIEWER_COUNT = 6;

    @Override
    public void run(String... args) {
        for (int i = 1; i <= REVIEWER_COUNT; i++) {
            String id = "reviewer" + i;
            if (memberRepository.existsById(id)) {
                continue;
            }
            Member m = new Member();
            m.setMemberId(id);
            m.setMemberPw(passwordEncoder.encode("pass"));
            m.setMemberName("리뷰어" + i);
            m.setMemberGender("남성");
            m.setEmail(id + "@naver.com");
            m.setDeptName("총괄");
            m.setPosition("리뷰어");
            m.setAdminYn(true);   // 모든 통계/기능 열람 가능하도록
            m.setAcceptYn(true);  // 승인 없이 즉시 로그인 가능
            m.setReviewerYn(true); // 쓰기 액션 차단 플래그
            memberRepository.save(m);
            log.info("리뷰어 계정 생성: {}", id);
        }
    }
}

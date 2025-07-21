package com.mutedcritics.member.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mutedcritics.entity.Member;
import com.mutedcritics.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository repo;
    private final PasswordEncoder encoder;
    private Map<String, String> saveAuthCode = new HashMap<>(); // 인증 코드 저장소

    // 이메일 발송 설정 (발신자 정보)
    @Value("${sender.email}")
    private String SENDER_EMAIL;
    @Value("${sender.password}")
    private String SENDER_PASSWORD;

    // 로그인
    public boolean login(String member_id, String member_pw) {
        Member member = repo.findById(member_id).orElse(null);
        if (member == null) {
            return false;
        }

        // 계정 승인 여부 확인 - 미승인 계정은 로그인 불가
        if (!member.isAcceptYn()) {
            return false;
        }

        String hash = member.getMemberPw();
        return encoder.matches(member_pw, hash);
    }

    // 중복 확인
    public boolean overlayId(String member_id) {
        return repo.existsById(member_id);
    }

    // 회원 정보 조회
    public Member getMemberById(String member_id) {
        return repo.findById(member_id).orElse(null);
    }

    // 회원가입
    public boolean join(Member params) {
        if (params.getMemberPw() == null) {
            return false;
        }

        // 비밀번호 암호화
        String hash = encoder.encode(params.getMemberPw());
        params.setMemberPw(hash);
        Member member = repo.save(params);
        return member != null;
    }

    // 인증 코드 발송
    public boolean emailcode(String member_id, String email) {
        if (member_id == null || email == null) {
            return false;
        }

        // 회원 정보 확인
        Member member = repo.findById(member_id).orElse(null);
        if (member == null) {
            return false;
        }

        // API 요청에서 받은 이메일 사용
        log.info("API 요청 이메일 사용: {}", email);

        // 인증 코드 생성 (6자리 숫자)
        String authCode = getAuthCode();
        log.info("인증 코드 생성: {}", authCode);

        // 인증 코드 저장
        saveAuthCode.put(member_id, authCode);

        // 실제 이메일 발송
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.naver.com"); // 네이버 SMTP 서버
            props.put("mail.smtp.port", "587"); // 네이버 TLS 포트 사용
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true"); // TLS 사용
            props.put("mail.smtp.ssl.trust", "smtp.naver.com");
            props.put("mail.debug", "true"); // 디버깅 활성화

            // 세션 생성 (발신자 인증 정보 사용)
            Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
                }
            });

            // 메시지 생성
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email)); // API 요청에서 받은 이메일 사용
            message.setSubject("Muted Critics 인증 코드 안내");

            // 메시지 내용 (HTML 형식)
            String htmlContent = "<div style='margin:20px;'>" +
                    "<p style='font-size:24px; padding:10px; background-color:#f8f9fa; border-radius:5px; margin:10px 0; display:inline-block;'>Muted Critics 인증 코드 안내</p>"
                    +
                    "<div style='font-size:24px; padding:10px; background-color:#f8f9fa; border-radius:5px; margin:10px 0; display:inline-block;'>"
                    +
                    authCode +
                    "</div>" +
                    "</div>";

            message.setContent(htmlContent, "text/html; charset=utf-8");

            // 메시지 발송
            Transport.send(message);

            log.info("인증 코드 발송 완료: {}", email);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 인증 코드 생성
    private String getAuthCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 100000 ~ 999999
        return String.valueOf(code);
    }

    // 인증 코드 검증
    public boolean verifyAuthCode(String member_id, String inputCode) {
        if (member_id == null || inputCode == null) {
            return false;
        }

        // 저장된 인증 코드 가져오기
        String savedCode = saveAuthCode.get(member_id);

        // 저장된 인증 코드가 없는 경우
        if (savedCode == null) {
            log.warn("저장된 인증 코드 없음: member_id={}", member_id);
            return false;
        }

        // 인증 비교
        boolean isMatch = savedCode.equals(inputCode);

        // 인증 성공 시 저장된 인증 코드 삭제 (재사용 방지)
        if (isMatch) {
            saveAuthCode.remove(member_id);
            log.info("인증 코드 검증 성공: member_id={}", member_id);
        } else {
            log.warn("인증 코드 불일치: member_id={}, 입력={}, 저장={}", member_id, inputCode, savedCode);
        }

        return isMatch;
    }

    // 비밀번호 변경
    public boolean changePassword(String member_id, String new_password) {
        if (member_id == null || new_password == null) {
            return false;
        }

        try {
            // 회원 정보 조회
            Member member = repo.findById(member_id).orElse(null);
            if (member == null) {
                return false;
            }

            // 새 비밀번호 암호화
            String encodedPassword = encoder.encode(new_password);

            // 비밀번호 업데이트
            member.setMemberPw(encodedPassword);
            repo.save(member);

            log.info("비밀번호 변경 성공: member_id={}", member_id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 관리자 권한 부여
    public boolean grant_admin(String member_id) {
        Member member = repo.findById(member_id).orElse(null);
        if (member == null) {
            return false;
        }
        member.setAdminYn(true);
        repo.save(member);
        return true;
    }

    // 관리자 권한 박탈
    public boolean revoke_admin(String member_id) {
        Member member = repo.findById(member_id).orElse(null);
        if (member == null) {
            return false;
        }
        member.setAdminYn(false);
        repo.save(member);
        return true;
    }

    // 관리자 권한 확인
    public boolean isAdmin(String member_id) {
        Member member = repo.findById(member_id).orElse(null);
        if (member == null) {
            return false;
        }
        return member.isAdminYn();
    }
    
    // 계정 승인 거절
    public boolean rejectMember(String member_id) {
        try {
            // 회원 정보 조회
            if (!repo.existsById(member_id)) {
                log.warn("회원 삭제 실패: 존재하지 않는 계정 - {}", member_id);
                return false;
            }
            
            // 회원 삭제
            repo.deleteById(member_id);
            log.info("회원 삭제 성공: {}", member_id);
            return true;
        } catch (Exception e) {
            log.error("회원 삭제 중 오류 발생: {}", e.getMessage(), e);
            return false;
        }
    }
    
}

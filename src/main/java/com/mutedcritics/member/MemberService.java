package com.mutedcritics.member;

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
    private Map<String, String> saveAuthCode = new HashMap<>(); // 인증 코드 저장소 (실제 프로덕션에서는 Redis 등 사용 권장)
    
    // 이메일 발송 설정 (발신자 정보)
    private final String SENDER_EMAIL = "oldslfov1234@naver.com"; // 발신자 이메일 (실제 네이버 계정으로 변경)
    private final String SENDER_PASSWORD = "2MDPBSG8WCT5";     // 발신자 비밀번호 (실제 비밀번호로 변경)
    
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
        if (params.getMemberPw() == null) {return false;}
        
        // 비밀번호 암호화
        String hash = encoder.encode(params.getMemberPw());
        params.setMemberPw(hash);
        Member member = repo.save(params);
        return member != null;
    }
    
    // 인증 코드 발송
    public boolean emailcode(String member_id, String email) {
        if (member_id == null || email == null) {return false;}
        
        // 회원 정보 확인
        Member member = repo.findById(member_id).orElse(null);
        if (member == null) {
            log.warn("회원 정보 없음: member_id={}", member_id);
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
            log.info("이메일 발송 시작: SMTP 서버 설정");
            
            // SMTP 서버 설정 (네이버 메일용)
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.naver.com"); // 네이버 SMTP 서버
            props.put("mail.smtp.port", "587"); // 네이버 TLS 포트 사용
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true"); // TLS 사용
            props.put("mail.smtp.ssl.trust", "smtp.naver.com");
            props.put("mail.debug", "true"); // 디버깅 활성화
            
            log.info("세션 생성 시작");
            
            // 세션 생성 (발신자 인증 정보 사용)
            Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
                }
            });
            
            log.info("메시지 생성 시작");
            
            // 메시지 생성
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email)); // API 요청에서 받은 이메일 사용
            message.setSubject("인증 코드 안내");
            
            // 메시지 내용 (HTML 형식)
            String htmlContent = 
                    "<div style='margin:20px;'>" +
                    "<p style='font-size:24px; padding:10px; background-color:#f8f9fa; border-radius:5px; margin:10px 0; display:inline-block;'>Muted Critics 인증 코드 안내</p>" +
                    "<div style='font-size:24px; padding:10px; background-color:#f8f9fa; border-radius:5px; margin:10px 0; display:inline-block;'>" +
                    authCode +
                    "</div>" +
                    "</div>";
            
            message.setContent(htmlContent, "text/html; charset=utf-8");
            
            log.info("메시지 발송 시작");
            
            // 메시지 발송
            Transport.send(message);
            
            log.info("인증 코드 발송 완료: {}", email);
            return true;
        } catch (MessagingException e) {
            log.error("이메일 발송 실패: {}", e.getMessage());
            e.printStackTrace(); // 스택 트레이스 출력
            return false;
        }
    }
    
    // 인증 코드 생성
    private String getAuthCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 100000 ~ 999999
        return String.valueOf(code);
    }
}

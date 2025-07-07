package com.mutedcritics.member;

import java.util.HashMap;
import java.util.List;
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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepo repo;
    private final PasswordEncoder encoder;
    private Map<String, String> saveAuthCode = new HashMap<>(); // 인증 코드 저장소

    // 이메일 발송 설정 (발신자 정보)
    @Value("${sender_email}")
    private String SENDER_EMAIL;
    @Value("${sender_password}")
    private String SENDER_PASSWORD;

    // 로그인
    public boolean login(String member_id, String member_pw) {
        Member member = repo.findById(member_id).orElse(null);
        if (member == null) {
            return false;
        }
        String hash = member.getMemberPw();
        return encoder.matches(member_pw, hash);
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

        // 인증 코드 비교
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
    
    // 회원 리스트 조회 (페이징, 검색, 정렬)
    public Map<String, Object> memberList(int page, int size, String keyword, String sortField, String sortDirection) {
        try {
            // 결과를 담을 맵
            Map<String, Object> result = new HashMap<>();
            
            // 페이지 번호는 0부터 시작 (1페이지 -> 0)
            int pageNumber = page - 1;
            if (pageNumber < 0) pageNumber = 0;
            
            // 검색어가 없으면 전체 회원 수 조회
            long totalCount;
            List<Member> members;
            
            if (keyword == null || keyword.trim().isEmpty()) {
                // 검색어 없음 - 전체 회원 리스트
                totalCount = repo.countAllMembers();
                members = repo.findAllWithPagingAndSorting(pageNumber, size, sortField, sortDirection);
            } else {
                // 검색어 있음 - 필터링된 회원 리스트
                totalCount = repo.countMembersByKeyword(keyword);
                members = repo.findByKeywordWithPagingAndSorting(keyword, pageNumber, size, sortField, sortDirection);
            }
            
            int totalPages = (int) Math.ceil((double) totalCount / 10);
            
            result.put("members", members);
            result.put("currentPage", page);
            result.put("totalItems", totalCount);
            result.put("totalPages", totalPages);
            return result;
            
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("error", e.getMessage());
            return errorResult;
        }
    }
}

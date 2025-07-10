package com.mutedcritics.mail.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

import com.google.api.client.util.Value;
import com.mutedcritics.entity.Mail;
import com.mutedcritics.entity.MailTemplate;
import com.mutedcritics.entity.Member;
import com.mutedcritics.mail.repository.MailRepository;
import com.mutedcritics.mail.repository.MailTemplateRepository;
import com.mutedcritics.mail.repository.UserRepository;
import com.mutedcritics.member.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailService {

    // 이메일 발송 설정 (발신자 정보)
    @Value("${sender.email}")
    private String SENDER_EMAIL;
    @Value("${sender.password}")
    private String SENDER_PASSWORD;

    private final MailRepository mailRepo;
    private final MailTemplateRepository mailTemplateRepo;
    private final MemberRepository memberRepo;
    private final UserRepository userRepo;

    // 이메일 발송
    public boolean sendMail(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();

        // 요청 파라미터 추출
        String memberId = (String) params.get("member_id");
        boolean isToAll = (boolean) params.get("is_to_all");
        String recipient = (String) params.get("recipient");
        String mailSub = (String) params.get("mail_sub");
        String mailContent = (String) params.get("mail_content");

        // 요청한 회원 아이디 찾기
        
        Member member = memberRepo.findById(memberId)
            .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다: " + memberId));

        // 메일 정보 저장
        Mail mail = new Mail();
        mail.setMember(member);
        mail.setToAll(isToAll);
        mail.setRecipient(recipient);
        mail.setMailSub(mailSub);
        mail.setMailContent(mailContent);
        List<String> recipients = new ArrayList<>();

        // 메일 수신자 찾기
        if (!mail.getRecipient().contains("@")) {
            recipients = userRepo.findUserIdsByUserType(mail.getRecipient());
        } else {
            recipients.add(mail.getRecipient());
        }

        // 메일 발송
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
            for (String recip : recipients) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(recip));
            }
            message.setSubject(mail.getMailSub());

            // 메시지 내용 (HTML 형식)
            String htmlContent = mail.getMailContent();

            message.setContent(htmlContent, "text/html; charset=utf-8");

            // 메시지 발송
            Transport.send(message);


            
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }

    }
}

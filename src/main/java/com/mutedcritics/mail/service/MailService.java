package com.mutedcritics.mail.service;

import java.time.LocalDate;
import java.util.ArrayList;
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
import com.mutedcritics.entity.AutoSend;
import com.mutedcritics.entity.Mail;
import com.mutedcritics.entity.MailTemplate;
import com.mutedcritics.entity.Member;
import com.mutedcritics.mail.repository.AutoSendRepository;
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
    private final AutoSendRepository autoSendRepo;
    
    // 이메일 발송 및 저장
    public boolean sendMail(Map<String, Object> params) {

        // 요청 파라미터 추출
        int temIdx = (int) params.get("temIdx");
        String memberId = (String) params.get("memberId");
        boolean isToAll = (boolean) params.get("isToAll");
        String recipient = (String) params.get("recipient");
        String mailSub = (String) params.get("mailSub");
        String mailContent = (String) params.get("mailContent");

        // 요청한 회원 아이디 찾기
        Member member = memberRepo.findById(memberId)
            .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다: " + memberId));
        // 요청한 템플릿 찾기
        MailTemplate mailTemplate = mailTemplateRepo.findById(temIdx)
            .orElseThrow(() -> new RuntimeException("템플릿을 찾을 수 없습니다: " + temIdx));

        // 보낼 메일 정보 저장
        Mail mail = new Mail();
        mail.setMailSub(mailSub);
        mail.setMailContent(mailContent);
        mail.setMember(member);
        mail.setMailTemplate(mailTemplate);
        mail.setToAll(isToAll);
        mail.setRecipient(recipient);

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

            // 메일 저장
            mailRepo.save(mail);

            return true;

        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 주기적 이메일 발송 및 저장
    public boolean sendMailInterval(Map<String, Object> params) {

        // 요청 파라미터 추출
        int temIdx = (int) params.get("temIdx");
        String memberId = (String) params.get("memberId");
        boolean isToAll = (boolean) params.get("isToAll");
        String recipient = (String) params.get("recipient");
        String mailSub = (String) params.get("mailSub");
        String mailContent = (String) params.get("mailContent");
        int intervalDays = (int) params.get("intervalDays");
        boolean isActive = (boolean) params.get("isActive");

        // 요청한 회원 아이디 찾기
        Member member = memberRepo.findById(memberId)
            .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다: " + memberId));
        // 요청한 템플릿 찾기
        MailTemplate mailTemplate = mailTemplateRepo.findById(temIdx)
            .orElseThrow(() -> new RuntimeException("템플릿을 찾을 수 없습니다: " + temIdx));

        // 보낼 메일 정보 저장
        AutoSend autoSend = new AutoSend();
        autoSend.setMailTemplate(mailTemplate);
        autoSend.setMember(member);
        autoSend.setToAll(isToAll);
        autoSend.setRecipient(recipient);
        autoSend.setMailSub(mailSub);
        autoSend.setMailContent(mailContent);
        autoSend.setIntervalDays(intervalDays);
        autoSend.setActive(isActive);

        List<String> recipients = new ArrayList<>();

        // 메일 수신자 찾기
        if (!autoSend.getRecipient().contains("@")) {
            recipients = userRepo.findUserIdsByUserType(autoSend.getRecipient());
        } else {
            recipients.add(autoSend.getRecipient());
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
            message.setSubject(autoSend.getMailSub());

            // 메시지 내용 (HTML 형식)
            String htmlContent = autoSend.getMailContent();

            message.setContent(htmlContent, "text/html; charset=utf-8");

            // 메시지 발송
            Transport.send(message);

            // 최초 1번 메일 저장
            if (params.get("nextSendDate") == null) {
                autoSend.setNextSendDate(LocalDate.now().plusDays(autoSend.getIntervalDays()));
                autoSendRepo.save(autoSend);
            }

            return true;

        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }

    }

    // 메일 템플릿 불러오기
    public Object getMailTemplate(int temIdx) {

        MailTemplate mailTemplate = mailTemplateRepo.findById(temIdx)
            .orElseThrow(() -> new RuntimeException("템플릿을 찾을 수 없습니다: " + temIdx));

        return mailTemplate;
    }
}

package com.mutedcritics.mail.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import com.mutedcritics.entity.AutoSend;
import com.mutedcritics.entity.Mail;
import com.mutedcritics.entity.MailTemplate;
import com.mutedcritics.entity.Member;
import com.mutedcritics.mail.repository.AutoSendRepository;
import com.mutedcritics.mail.repository.MailRepository;
import com.mutedcritics.mail.repository.MailTemplateRepository;
import com.mutedcritics.mail.repository.UserRepository;
import com.mutedcritics.member.repository.MemberRepository;

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
            if(mail.getRecipient().equals("전체")){
                recipients = userRepo.findAllUserIds();
            }else{
                recipients = userRepo.findUserIdsByUserType(mail.getRecipient());
            }
        } else {
            String[] arr = StringUtils.commaDelimitedListToStringArray(mail.getRecipient());
            recipients.addAll(Arrays.stream(arr)
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.toList()));
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
            if (recipients.size() > 0 && recipients != null) {
                for (String recip : recipients) {
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(recip));
                }
            }else{
                log.error("메일 발송 실패: 수신자가 없습니다.");
                return false;
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
        int intervalDays = 0;
        if (params.get("intervalDays") != null) {
            intervalDays = (int) params.get("intervalDays");
        }
        boolean isActive = false;
        if (params.get("isActive") != null) {
            isActive = (boolean) params.get("isActive");
        }
        LocalDate nextSendDate = null;
        if (params.get("nextSendDate") != null) {
            String nextSendDateStr = params.get("nextSendDate").toString();
            nextSendDate = LocalDate.parse(nextSendDateStr);
        }
        LocalDate reservedDate = null;
        if (params.get("reservedDate") != null) {
            String reservedDateStr = params.get("reservedDate").toString();
            reservedDate = LocalDate.parse(reservedDateStr);
        }

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
            if(autoSend.getRecipient().equals("전체")){
                recipients = userRepo.findAllUserIds();
            }else{
                recipients = userRepo.findUserIdsByUserType(autoSend.getRecipient());
            }
        } else {
            String[] arr = StringUtils.commaDelimitedListToStringArray(autoSend.getRecipient());
            recipients.addAll(Arrays.stream(arr)
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.toList()));
        }

        // 예약 날짜가 있고 다음 발송일이 없다 == 최초 정기메일이자 예약메일, 메일 당장 안보내고 예약 날짜로 다음 발송일 설정
        if (reservedDate != null && nextSendDate == null) {
            autoSend.setNextSendDate(reservedDate);
            autoSendRepo.save(autoSend);
            return true;
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
            if (recipients.size() > 0 && recipients != null) {
                for (String recip : recipients) {
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(recip));
                }
            }else{
                log.error("메일 발송 실패: 수신자가 없습니다.");
                return false;
            }
            message.setSubject(autoSend.getMailSub());

            // 메시지 내용 (HTML 형식)
            String htmlContent = autoSend.getMailContent();

            message.setContent(htmlContent, "text/html; charset=utf-8");

            // 메시지 발송
            Transport.send(message);

            // 다음 발송일이 없다 == 최초 정기메일, 메일 정보 저장 후 다음 발송일 설정
            if (nextSendDate == null) {
                autoSend.setNextSendDate(LocalDate.now().plusDays(intervalDays));
                autoSendRepo.save(autoSend);
            }

            // 메일 발송 성공 시 히스토리 저장
            Mail mail = new Mail();
            mail.setMailSub(autoSend.getMailSub());
            mail.setMailContent(autoSend.getMailContent());
            mail.setMember(autoSend.getMember());
            mail.setMailTemplate(autoSend.getMailTemplate());
            mail.setToAll(autoSend.isToAll());
            mail.setRecipient(autoSend.getRecipient());
            mailRepo.save(mail);

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

    // 메일 발송 목록 조회
    public Page<Mail> getMailList(int page, String align) {

        Pageable pageable = null;
        Page<Mail> mailList = null;

        // 일반메일 목록 조회일 경우, 내림차순 정렬일 경우
        if ("dateDesc".equals(align)) {
            pageable = PageRequest.of(page - 1, 15, Sort.by("mailDate").descending());
            mailList = mailRepo.findAll(pageable);
        // 일반메일 목록 조회일 경우, 오름차순 정렬일 경우
        } else if ("dateAsc".equals(align)) {
            pageable = PageRequest.of(page - 1, 15, Sort.by("mailDate").ascending());
            mailList = mailRepo.findAll(pageable);
        }

        return mailList;
    }
    // 정기메일 목록 조회
    public Page<AutoSend> getAutoSendList(int page, String align) {

        Pageable pageable = null;
        Page<AutoSend> autoSendList = null;

        // 정기메일 목록 조회일 경우, 내림차순 정렬일 경우
        if ("dateDesc".equals(align)) {
            pageable = PageRequest.of(page - 1, 15, Sort.by("createdAt").descending());
            autoSendList = autoSendRepo.findAll(pageable);
        // 정기메일 목록 조회일 경우, 오름차순 정렬일 경우
        } else if ("dateAsc".equals(align)) {
            pageable = PageRequest.of(page - 1, 15, Sort.by("createdAt").ascending());
            autoSendList = autoSendRepo.findAll(pageable);
        }

        return autoSendList;
    }

    // 메일 발송 목록 검색
    public Page<Mail> searchMailList(String search, String searchType, int page) {

        Pageable pageable = PageRequest.of(page - 1, 15, Sort.by("mailDate").descending());
        Page<Mail> mailList = null;

        // 일반메일 목록 검색이고 메일 제목 검색일 경우
        if("mailSub".equals(searchType)) {
            mailList = mailRepo.findByMailSubContaining(search, pageable);
        // 회원 아이디 검색일 경우
        } else if("memberId".equals(searchType)) {
            mailList = mailRepo.findByMemberIdContaining(search, pageable);
        }
        // 수신군 검색일 경우
        else if("recipient".equals(searchType)) {
            mailList = mailRepo.findByRecipientContaining(search, pageable);
        }

        if (mailList == null) {
            mailList = Page.empty();
        }

        return mailList;
    }

    // 정기메일 목록 검색
    public Page<AutoSend> searchAutoSendList(String search, String searchType, int page) {

        Pageable pageable = null;
        pageable = PageRequest.of(page - 1, 15, Sort.by("createdAt").descending());
        Page<AutoSend> autoSendList = null;

        if("mailSub".equals(searchType)) {
            autoSendList = autoSendRepo.findByMailSubContaining(search, pageable);
        } else if("memberId".equals(searchType)) {
            autoSendList = autoSendRepo.findByMemberIdContaining(search, pageable);
        } else if("recipient".equals(searchType)) {
            autoSendList = autoSendRepo.findByRecipientContaining(search, pageable);
        }

        if (autoSendList == null) {
            autoSendList = Page.empty();
        }

        return autoSendList;
    }

    // 메일 상세보기
    public Mail getMailDetail(int mailIdx) {

        Mail mail = mailRepo.findById(mailIdx)
            .orElseThrow(() -> new RuntimeException("메일을 찾을 수 없습니다: " + mailIdx));

        return mail;
    }

    // 정기메일 상세보기    
    public AutoSend getAutoSendMailDetail(int scheduleIdx) {

        AutoSend autoSend = autoSendRepo.findById(scheduleIdx)
            .orElseThrow(() -> new RuntimeException("정기메일을 찾을 수 없습니다: " + scheduleIdx));

        return autoSend;
    }

    // 정기 메일 수정하기
    public boolean updateMail(Map<String, Object> params) {

        boolean success = false;
        
        // 요청 파라미터 추출
        int scheduleIdx = (int) params.get("scheduleIdx");
        int temIdx = (int) params.get("temIdx");
        String memberId = (String) params.get("memberId");
        boolean isToAll = (boolean) params.get("isToAll");
        String recipient = (String) params.get("recipient");
        String mailSub = (String) params.get("mailSub");
        String mailContent = (String) params.get("mailContent");
        int intervalDays = (int) params.get("intervalDays");
        boolean isActive = (boolean) params.get("isActive");
        String nextSendDateStr = params.get("nextSendDate").toString();
        LocalDate nextSendDate = LocalDate.parse(nextSendDateStr);

        // 요청한 회원 아이디 찾기
        Member member = memberRepo.findById(memberId)
            .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다: " + memberId));
        // 요청한 템플릿 찾기
        MailTemplate mailTemplate = mailTemplateRepo.findById(temIdx)
            .orElseThrow(() -> new RuntimeException("템플릿을 찾을 수 없습니다: " + temIdx));
        // 요청한 정기메일 찾기
        AutoSend autoSend = autoSendRepo.findById(scheduleIdx)
            .orElseThrow(() -> new RuntimeException("정기메일을 찾을 수 없습니다: " + scheduleIdx));

        // 요청한 정기메일 수정
        autoSend.setMailTemplate(mailTemplate);
        autoSend.setMember(member);
        autoSend.setToAll(isToAll);
        autoSend.setRecipient(recipient);
        autoSend.setMailSub(mailSub);
        autoSend.setMailContent(mailContent);
        autoSend.setIntervalDays(intervalDays);
        autoSend.setActive(isActive);
        autoSend.setNextSendDate(nextSendDate);
        
        try {
            autoSendRepo.save(autoSend);
            success = true;
        } catch (Exception e) {
            log.error("정기메일 수정 중 오류 발생: {}", e.getMessage(), e);
        }

        return success;
    }
}

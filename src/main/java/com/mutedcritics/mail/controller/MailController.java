package com.mutedcritics.mail.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mutedcritics.dto.AutoSendDTO;
import com.mutedcritics.dto.MailDTO;
import com.mutedcritics.entity.AutoSend;
import com.mutedcritics.entity.Mail;
import com.mutedcritics.mail.service.MailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@CrossOrigin
@RestController
@Slf4j
@RequiredArgsConstructor
public class MailController {

    private final MailService service;

    private Map<String, Object> resp = null;

    // 메일 발송
    @PostMapping("/mail/send")
    public Map<String, Object> sendMail(@RequestBody Map<String, Object> params) {

        resp = new HashMap<>();

        boolean success = false;

        if ((params.get("mailInterval") == null || (int) params.get("mailInterval") == 0)
                && params.get("reservedDate") == null) {
            success = service.sendMail(params);
        } else {
            success = service.sendMailInterval(params);
        }

        resp.put("success", success);

        return resp;
    }

    // 메일 템플릿 불러오기
    @GetMapping("/mail/template")
    public Map<String, Object> getMailTemplate(@RequestParam int temIdx) {

        resp = new HashMap<>();

        if (temIdx > 0) {
            resp.put("template", service.getMailTemplate(temIdx));
        } else {
            resp.put("template", "잘못된 템플릿 번호 입니다.");
        }

        return resp;
    }

    // 메일 발송 목록 조회
    @GetMapping("/mail/list")
    public Map<String, Object> getMailList(
        @RequestParam String sort,
        @RequestParam int page,
        @RequestParam String align) {
    
        resp = new HashMap<>();
    
        // 메일 발송 목록일경우
        if ("mailList".equals(sort)) {
            Page<Mail> mails = service.getMailList(page, align);
            Page<MailDTO> mailDTOs = mails.map(mail -> new MailDTO(mail));
            resp.put("mailList", mailDTOs);
        // 정기메일 발송 목록일경우
        } else if ("autoSendList".equals(sort)) {
            Page<AutoSend> autoSends = service.getAutoSendList(page, align);
            Page<AutoSendDTO> autoSendDTOs = autoSends.map(autoSend -> new AutoSendDTO(autoSend));
            resp.put("autoSendList", autoSendDTOs);
        }
    
        return resp;
    }

    // 메일 발송 목록 검색
    @GetMapping("/mail/search")
    public Map<String, Object> searchMailList(
            @RequestParam String search,
            @RequestParam String searchType,
            @RequestParam int page,
            @RequestParam(required = false) String sort) {

        resp = new HashMap<>();

        if ("autoSendList".equals(sort)) {
            Page<AutoSend> autoSends = service.searchAutoSendList(search, searchType, page);
            Page<AutoSendDTO> autoSendDTOs = autoSends.map(autoSend -> new AutoSendDTO(autoSend));
            resp.put("autoSendSearchResult", autoSendDTOs);
        } else {
            Page<Mail> mails = service.searchMailList(search, searchType, page);
            Page<MailDTO> mailDTOs = mails.map(mail -> new MailDTO(mail));
            resp.put("mailSearchResult", mailDTOs);
        }

        return resp;
    }

    // 메일 상세보기
    @GetMapping("/mail/detail")
    public Map<String, Object> getMailDetail(
            @RequestParam(required = false) Integer mailIdx,
            @RequestParam(required = false) Integer scheduleIdx) {

        resp = new HashMap<>();

        // 파라미터 유효성 검사
        if ((mailIdx == null || mailIdx <= 0) && (scheduleIdx == null || scheduleIdx <= 0)) {
            resp.put("status", "error");
            resp.put("message", "mailIdx 또는 scheduleIdx 중 하나는 반드시 제공되어야 합니다.");
            return resp;
        }

        // 메일 상세보기
        if (mailIdx != null && mailIdx > 0) {
            Mail mail = service.getMailDetail(mailIdx);
            MailDTO mailDTO = new MailDTO(mail);
            resp.put("mail", mailDTO);
        // 정기 메일 상세보기
        } else if (scheduleIdx != null && scheduleIdx > 0) {
            AutoSend autoSend = service.getAutoSendMailDetail(scheduleIdx);
            AutoSendDTO autoSendDTO = new AutoSendDTO(autoSend);
            resp.put("autoSend", autoSendDTO);
        }

        return resp;
    }

    // 정기 메일 수정하기
    @PutMapping("/mail/update/{scheduleIdx}")
    public Map<String, Object> updateMail(
            @PathVariable int scheduleIdx,
            @RequestBody Map<String, Object> params) {

        resp = new HashMap<>();

        params.put("scheduleIdx", scheduleIdx);

        boolean success = service.updateMail(params);

        resp.put("success", success);

        return resp;
    }

}

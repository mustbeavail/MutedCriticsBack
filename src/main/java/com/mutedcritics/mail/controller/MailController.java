package com.mutedcritics.mail.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mutedcritics.mail.service.MailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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

        if ((params.get("mailInterval") == null || (int)params.get("mailInterval") == 0)
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

        if(temIdx > 0) {
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

        resp = service.getMailList(sort, page, align);

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

        resp = service.searchMailList(search, searchType, page, sort);

        return resp;
    }

    // 메일 상세보기
    // 정기 메일 수정하기
    // 메일 삭제하기? 할까말까
}

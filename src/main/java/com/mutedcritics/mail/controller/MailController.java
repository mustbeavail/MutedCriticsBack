package com.mutedcritics.mail.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping("/mail/send")
    public Map<String, Object> sendMail(@RequestBody Map<String, Object> params) {

        resp = new HashMap<>();

        boolean success = service.sendMail(params);

        resp.put("success", success);

        return resp;
    }

}

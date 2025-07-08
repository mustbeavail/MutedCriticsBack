package com.mutedcritics.inquiry.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mutedcritics.entity.Inquiry;
import com.mutedcritics.inquiry.service.AgentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/agent")
public class AgentController {

    private final AgentService service;

}

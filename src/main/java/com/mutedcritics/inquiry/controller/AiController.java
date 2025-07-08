package com.mutedcritics.inquiry.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mutedcritics.entity.Inquiry;
import com.mutedcritics.inquiry.service.AiService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/ai")
public class AiController {

    private final AiService service;

    @PostMapping("/process-unanswered")
    public Map<String, Object> processUnansweredInquiries() {
        Map<String, Object> result = new HashMap<>();

        try {
            service.processUnansweredInquiries();
            result.put("success", true);
            result.put("msg", "답변이 없는 문의/신고 AI 자동 답변 완료");
        } catch (Exception e) {
            log.error("AI 자동 답변 처리 중 오류 발생", e);
            result.put("success", false);
            result.put("msg", "처리 중 오류가 발생했습니다." + e.getMessage());
        }

        return result;
    }

    // findUnansweredInquiries 메서드 테스트용 엔드포인트
    @PostMapping("/test-unanswered")
    public Map<String, Object> testUnansweredInquiries() {
        Map<String, Object> result = new HashMap<>();

        try {
            List<Inquiry> unansweredList = service.testFindUnansweredInquiries();
            result.put("success", true);
            result.put("count", unansweredList.size());
            result.put("msg", "답변이 없는 문의/신고 조회 완료 - 총 " + unansweredList.size() + "건");
        } catch (Exception e) {
            log.error("답변이 없는 문의/신고 조회 테스트 중 오류 발생", e);
            result.put("success", false);
            result.put("msg", "조회 중 오류가 발생했습니다: " + e.getMessage());
        }

        return result;
    }

}

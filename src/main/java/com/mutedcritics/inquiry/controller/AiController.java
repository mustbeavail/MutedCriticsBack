package com.mutedcritics.inquiry.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.mutedcritics.dto.ResponseDTO;
import com.mutedcritics.inquiry.service.AiService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j

@RestController
@RequiredArgsConstructor
public class AiController {

    private final AiService service;

    // 상담사 지원용 AI 답변 생성
    @GetMapping("/inquiry/{inquiryIdx}/ai-response")
    public ResponseEntity<?> generateAiResponse(@PathVariable Integer inquiryIdx) {
        Map<String, Object> result = new HashMap<>();
        try {
            String aiResponse = service.generateAiResponseForAgent(inquiryIdx);
            result.put("success", true);
            result.put("response", aiResponse);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("AI 답변 생성 중 오류 발생 inquiryIdx={}", inquiryIdx, e);
            result.put("success", false);
            result.put("msg", "AI 답변 생성 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    // 상담사가 문의/신고에 대한 답변 작성(아까 받아온 ai 답변을 수정하여 답변 작성 후 저장)
    // 답변이 등록되었다면 status 를 완료로 변경
    @PostMapping("/inquiry/agent-response")
    public ResponseEntity<?> saveAgentResponse(@RequestBody ResponseDTO responseDTO) {
        Map<String, Object> result = new HashMap<>();
        try {
            String agentResponse = service.saveAgentResponse(responseDTO);
            result.put("success", true);
            result.put("response", agentResponse);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("상담사 답변 처리 중 오류 발생 inquiryId={}", responseDTO.getInquiryIdx(), e);
            result.put("success", false);
            result.put("msg", "상담사 답변 생성 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

}

package com.mutedcritics.inquiry.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.jayway.jsonpath.JsonPath;
import com.mutedcritics.entity.Inquiry;
import com.mutedcritics.entity.Response;
import com.mutedcritics.inquiry.repository.InquiryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class AiService {

    private final InquiryRepository repository;

    private final WebClient webClient;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    // 답변이 없는 모든 문의/신고를 배치 처리
    @Transactional
    public void processUnansweredInquiries() {

        log.info("답변이 없는 문의/신고 AI 자동 처리 시작");

        // 답변이 없는 문의/신고 조회
        List<Inquiry> unansweredInquiries = repository.findUnansweredInquiries();

        if (unansweredInquiries.isEmpty()) {
            log.info("처리할 답변 없는 문의/신고가 없습니다.");
            return;
        }

        log.info("처리할 답변 없는 문의/신고 개수 : {}", unansweredInquiries.size());

        int successCount = 0;
        int failCount = 0;

        for (Inquiry inquiry : unansweredInquiries) {
            try {
                String aiResponse = generateAiResponse(inquiry);
                createAiResponse(inquiry, aiResponse);

                successCount++;
                log.info("문의/신고 {} AI 자동 답변 완료 - 제목 : {}",
                        inquiry.getInquiryIdx(), inquiry.getTitle());

                Thread.sleep(1000);
            } catch (Exception e) {
                failCount++;
                log.error("문의/신고 {} AI 답변 처리 중 오류 발생 - 제목: {}, 오류: {}",
                        inquiry.getInquiryIdx(), inquiry.getTitle(), e.getMessage());
            }
        }

        log.info("AI 자동 답변 처리 완료 - 성공: {}, 실패: {}", successCount, failCount);

    }

    // 특정 문의/신고에 대한 AI 답변 생성
    private String generateAiResponse(Inquiry inquiry) {
        String prompt = createPrompt(inquiry);
        return callGeminiApi(prompt);
    }

    // 프롬프트 생성
    private String createPrompt(Inquiry inquiry) {
        StringBuilder prompt = new StringBuilder();

        // 역할 설정
        prompt.append("당신은 오버워치2 게임 고객 상담사입니다. ");

        // 문의/신고 타입에 따른 역할 조정
        if ("문의".equals(inquiry.getType())) {
            prompt.append("다음 문의에 대해 친절하고 전문적으로 답변해주세요.\n\n");
        } else {
            prompt.append("다음 신고에 대해 적절한 조치 방안을 전문적으로 안내해주세요.\n\n");
        }

        // 게임 정보
        prompt.append("우리가 서비스하는 게임은 오버워치2 입니다. 오버워치2 라는 게임에 맞게 답변하세요.\n\n");

        // 문의/신고 정보
        prompt.append("유형: ").append(inquiry.getType()).append("\n");
        prompt.append("카테고리 : ").append(inquiry.getCategory()).append("\n");
        prompt.append("제목 : ").append(inquiry.getTitle()).append("\n");
        prompt.append("내용 : ").append(inquiry.getContent()).append("\n");

        // 답변 가이드라인
        prompt.append("답변 가이드라인:\n");
        prompt.append("1. 한국어로 답변할 것.\n");
        prompt.append("2. 친절하고 전문적으로 답변할 것.\n");
        prompt.append("3. 500자 이내로 답변할 것.\n");

        if ("신고".equals(inquiry.getType())) {
            prompt.append("- 신고 접수 확인 및 검토 과정 안내\n");
            prompt.append("- 적절한 조치 방안 제시\n");
        }

        return prompt.toString();
    }

    // gemini 호출(WebClient)
    private String callGeminiApi(String prompt) {
        try {
            // 요청 본문 구성
            Map<String, Object> requestBody = createRequestBody(prompt);

            // webclient 로 호출
            Mono<Map> responseMono = webClient.post()
                    .uri(geminiApiUrl + "?key=" + geminiApiKey) // api url 과 key 설정
                    .contentType(MediaType.APPLICATION_JSON) // content-type 설정
                    .bodyValue(requestBody) // 요청 본문 설정
                    .retrieve() // 응답 받기
                    .bodyToMono(Map.class); // 응답 타입 지정

            // 비동기를 동기로 변환하여 응답 받기
            Map<String, Object> responseBody = responseMono.block();

            // 응답 파싱 (json path 사용)
            return parseGeminiResponse(responseBody);

        } catch (Exception e) {
            log.error("Gemini API 호출 실패 : {}", e.getMessage(), e);
            log.error("요청 URL : {}", geminiApiUrl + "?key=" + geminiApiKey);
            return getDefaultResponse();
        }
    }

    // Gemini API 요청 본문 생성
    private Map<String, Object> createRequestBody(String prompt) {
        Map<String, Object> requestBody = new HashMap<>();
        Map<String, Object> contents = new HashMap<>();
        Map<String, Object> parts = new HashMap<>();

        // Gemini API 가 요구하는 JSON 구조 생성
        parts.put("text", prompt);
        contents.put("parts", List.of(parts));
        requestBody.put("contents", List.of(contents));

        return requestBody;
    }

    // Gemini API 응답 파싱
    private String parseGeminiResponse(Map<String, Object> responseBody) {
        try {
            log.info("Gemini API 응답 : {}", responseBody);

            // JSON Path 사용: $.candidates[0].content.parts[0].text
            // 복잡한 중첩 구조를 한 줄로 처리!
            String text = JsonPath.read(responseBody, "$.candidates[0].content.parts[0].text");

            if (text != null && !text.trim().isEmpty()) {
                return text;
            }

            throw new RuntimeException("응답 텍스트가 비어있습니다.");
        } catch (Exception e) {
            log.error("Gemini API 응답 파싱 실패 : {}", e.getMessage(), e);
            return getDefaultResponse();
        }
    }

    // AI 답변을 Response 엔티티로 생성 및 저장
    private void createAiResponse(Inquiry inquiry, String content) {
        Response response = new Response();
        response.setInquiry(inquiry);
        response.setContent(content);
        response.setResType("AI");

        // response 를 inquiry 의 responses 리스트에 추가
        inquiry.getResponses().add(response);

        // 저장
        repository.save(inquiry);
    }

    // API 호출 실패 시 기본 응답
    private String getDefaultResponse() {
        return "안녕하세요. 문의해 주셔서 감사합니다. " +
                "관련 부서에서 검토 후 빠른 시일 내에 상세한 답변을 드리겠습니다. " +
                "추가 문의사항이 있으시면 언제든지 연락 주세요.";
    }

    // findUnansweredInquiries 메서드 테스트용
    public List<Inquiry> testFindUnansweredInquiries() {
        log.info("답변이 없는 문의/신고 조회 테스트 시작");
        List<Inquiry> result = repository.findUnansweredInquiries();
        log.info("답변이 없는 문의/신고 조회 결과 - 총 {}건", result.size());
        return result;
    }

}

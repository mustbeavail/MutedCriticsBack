package com.mutedcritics.inquiry.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.jayway.jsonpath.JsonPath;
import com.mutedcritics.dto.ResponseDTO;
import com.mutedcritics.entity.Inquiry;
import com.mutedcritics.entity.Member;
import com.mutedcritics.entity.Response;
import com.mutedcritics.inquiry.repository.InquiryRepository;
import com.mutedcritics.inquirystat.service.InquiryStatService;
import com.mutedcritics.member.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class AiService {

    private final InquiryRepository inquiryRepository;
    private final MemberRepository memberRepository;
    private final InquiryStatService inquiryStatService;

    private final WebClient webClient;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${sender.email}")
    private String SENDER_EMAIL;

    @Value("${sender.password}")
    private String SENDER_PASSWORD;

    // 상담사 답변시 알림 메일 발송
    public void sendAgentResponseMail(String agentId, int inquiryIdx) {
        try {
            // 문의/신고 게시글 조회(고객 아이디를 찾기 위해)
            Inquiry inquiry = inquiryRepository.findById(inquiryIdx)
                    .orElseThrow(() -> new RuntimeException("문의/신고가 존재하지 않습니다. inquiryIdx: " + inquiryIdx));

            // 수신자 정보 (고객)
            String recipient = inquiry.getUser().getUserId(); // 고객 이메일
            log.info("수신자 정보 : {}", recipient);

            // 메일 제목과 메일 내용
            String title = "고객님의 문의/신고 글에 답변이 등록되었습니다.";
            String content = "안녕하세요! 저희 Null Core 를 플레이해 주셔서 감사합니다.\n" +
                    "고객님이 남기신 문의/신고 글에 답변이 등록되었습니다.\n" +
                    "답변 내용은 다음과 같습니다.\n" +
                    "--------------------------------\n" +
                    inquiry.getResponse().getContent() +
                    "\n--------------------------------\n" +
                    "감사합니다.";

            // 메일 발송
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

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject(title);
            message.setText(content, "UTF-8");

            Transport.send(message);
            log.info("상담사 답변 메일 발송 성공 : {}", recipient);

        } catch (Exception e) {
            log.error("상담사 답변 메일 발송 실패 : {}", e.getMessage(), e);
        }

    }

    // 상담사 지원용 AI 답변 생성
    public String generateAiResponseForAgent(Integer inquiryIdx) {
        log.info("AI 답변 생성 요청 (상담사 지원) - Inquiry ID: {}", inquiryIdx);

        Inquiry inquiry = inquiryRepository.findById(inquiryIdx)
                .orElseThrow(() -> new RuntimeException("문의/신고가 존재하지 않습니다. inquiryIdx: " + inquiryIdx));

        return generateAiResponse(inquiry);
    }

    // 상담사 답변 생성 및 저장
    @Transactional
    public String saveAgentResponse(ResponseDTO responseDTO) {
        Inquiry inquiry = inquiryRepository.findById(responseDTO.getInquiryIdx())
                .orElseThrow(() -> new RuntimeException("문의/신고가 존재하지 않습니다. inquiryIdx: " + responseDTO.getInquiryIdx()));

        // 이미 답변이 완료된 경우 중복 답변 방지
        if ("완료".equals(inquiry.getStatus())) {
            throw new RuntimeException("이미 답변이 완료된 문의/신고입니다. inquiryIdx: " + responseDTO.getInquiryIdx());
        }

        Member agent = memberRepository.findById(responseDTO.getAgentId())
                .orElseThrow(() -> new RuntimeException("상담사가 존재하지 않습니다. agentId: " + responseDTO.getAgentId()));

        // 이전 상태 저장
        String previousStatus = inquiry.getStatus();

        Response response = inquiry.getResponse();
        if (response == null) {
            // 새로운 응답 생성
            response = new Response();
            response.setInquiry(inquiry);
            inquiry.setResponse(response);
        }

        // 응답 내용 업데이트
        response.setAgent(agent);
        response.setContent(responseDTO.getContent());
        inquiry.setStatus("완료");

        inquiryRepository.save(inquiry);

        // 상담사 답변 메일 발송
        sendAgentResponseMail(responseDTO.getAgentId(), responseDTO.getInquiryIdx());

        // 상태가 변경된 경우 통계 업데이트 (미처리 → 완료)
        if (!"완료".equals(previousStatus)) {
            log.info("문의/신고 상태 변경 감지 - ID: {}, 이전 상태: {}, 현재 상태: 완료",
                    inquiry.getInquiryIdx(), previousStatus);

            // 해당 문의/신고의 미처리 건수만 효율적으로 업데이트
            LocalDate createdDate = inquiry.getCreatedAt().toLocalDate();
            String ticketType = inquiry.getType();
            String category = inquiry.getCategory();

            try {
                inquiryStatService.updateUnresolvedCountAuto(createdDate, ticketType, category);
                log.info("미처리 건수 자동 업데이트 성공 - 문의/신고 ID: {}, 날짜: {}, 타입: {}, 카테고리: {}",
                        inquiry.getInquiryIdx(), createdDate, ticketType, category);
            } catch (Exception e) {
                log.error("미처리 건수 자동 업데이트 실패 - 문의/신고 ID: {}, 날짜: {}, 타입: {}, 카테고리: {}",
                        inquiry.getInquiryIdx(), createdDate, ticketType, category, e);
                // 통계 업데이트 실패가 메인 비즈니스 로직에 영향을 주지 않도록 예외를 다시 던지지 않음
            }
        }

        return responseDTO.getContent();
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
        prompt.append("4. 이모지나 특수문자는 사용하지 말 것.\n");

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

    // API 호출 실패 시 기본 응답
    private String getDefaultResponse() {
        return "안녕하세요. 문의해 주셔서 감사합니다. " +
                "관련 부서에서 검토 후 빠른 시일 내에 상세한 답변을 드리겠습니다. " +
                "추가 문의사항이 있으시면 언제든지 연락 주세요.";
    }

}

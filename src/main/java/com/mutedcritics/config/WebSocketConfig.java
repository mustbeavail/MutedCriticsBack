package com.mutedcritics.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import lombok.RequiredArgsConstructor;

/**
 * WebSocket 설정 클래스입니다.
 * STOMP 프로토콜을 사용하는 메시징 브로커를 활성화하고,
 * WebSocket 연결을 위한 엔드포인트를 등록합니다.
 */
@Configuration
@EnableWebSocketMessageBroker // STOMP 기반 WebSocket 메시지 처리를 활성화
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * 메시지 브로커 설정을 구성합니다.
     * 클라이언트 → 서버 → 클라이언트 간의 메시지 흐름을 정의합니다.
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 클라이언트 구독 경로(prefix): 서버 → 클라이언트로 메시지 보낼 때 사용하는 경로
        // 예: /topic/chat, /queue/notice 등
        config.enableSimpleBroker("/topic", "/queue");

        // 클라이언트가 서버에 메시지를 보낼 때 붙이는 prefix
        // 예: 클라이언트 → /app/chat 로 전송하면 서버 @MessageMapping("/chat")에 매핑
        config.setApplicationDestinationPrefixes("/app");

        // 특정 사용자에게 메시지를 보낼 때 사용되는 prefix
        // 예: /user/{username}/queue/alert
        config.setUserDestinationPrefix("/user");
    }

    /**
     * 클라이언트가 WebSocket을 연결할 수 있는 엔드포인트를 등록합니다.
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 웹소켓 연결 엔드포인트 설정
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*") // cors 설정
                .withSockJS(); // SockJS fallback 옵션 활성화

        // postman 등 테스트를 위한 엔드포인트 추가
        registry.addEndpoint("/ws-test")
                .setAllowedOriginPatterns("*");
    }

}

package com.mutedcritics.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 메시지 브로커 설정
        // "/topic" 으로 시작하는 메시지는 메시지 브로커로 라우팅
        config.enableSimpleBroker("/topic", "/queue");

        // 클라이언트에서 메시지를 보낼 때 사용할 prefix 설정
        config.setApplicationDestinationPrefixes("/app");

        // 개별 사용자에게 메시지를 보낼 때 사용할 prefix 설정
        config.setUserDestinationPrefix("/user");
    }

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

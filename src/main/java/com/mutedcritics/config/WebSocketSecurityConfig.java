package com.mutedcritics.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

/**
 * WebSocket 보안을 설정하는 클래스입니다.
 * Spring Security와 STOMP(WebSocket)를 연동하며,
 * 메시지 목적지에 대한 접근 권한을 설정합니다.
 */
@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    /**
     * WebSocket 메시지의 목적지(destinations)에 대한 보안 규칙을 설정합니다.
     */
    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
                .simpDestMatchers("/**").permitAll() // 모든 메시지 목적지에 대해 인증 없이 허용
                .anyMessage().permitAll(); // 그 외 메시지도 모두 허용
    }

    /**
     * 동일 출처 정책(Same-Origin Policy)을 비활성화합니다.
     * → 프론트엔드와 백엔드가 서로 다른 도메인/포트일 경우 WebSocket 연결을 허용하기 위해 필요합니다.
     * 예: 프론트엔드가 http://localhost:3000, 백엔드가 http://localhost:8080 인 경우
     */
    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}
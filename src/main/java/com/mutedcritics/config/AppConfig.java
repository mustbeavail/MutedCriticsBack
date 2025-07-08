package com.mutedcritics.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                // 메모리 버퍼 크기를 1MB로 설정
                // 왜 필요한가? Gemini API 응답이 클 수 있어서 기본값(256KB)보다 크게 설정
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 1024))
                .build();
    }

}

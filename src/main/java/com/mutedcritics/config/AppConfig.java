package com.mutedcritics.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                // 메모리 버퍼 크기를 256KB 로 설정
                // API 응답을 메모리 버퍼에 임시 저장을 하는데 그 사이즈를 설정
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(256 * 1024))
                .build();
    }

}

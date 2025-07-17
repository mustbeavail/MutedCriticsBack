package com.mutedcritics.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SecurityConfig implements WebMvcConfigurer {

    //암호화 기능을 반환하는 빈을 등록
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 빈 등록? -> 특정한 클래스나 객체를 등록하여 스프링에서 필요할때 자유롭게 쓸수 있도록 등록하는 행위
    // 예) Spring 의 경우 xml 에 등록되어 있고, Boot 에는 위와 같이 Config 클래스에 등록되어있다.
    // 또다른 예로 @Service 나 @Component 를 @AutoWired 하는 것도 빈 등록으로 볼 수 있다.

    // 스프링 시큐리티의 기본기능을 끈 채로 빈을 등록
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // ────────────────────────────────────────────────
                // 1) CORS(교차 출처) 필터 활성화
                //    - GlobalCorsConfig(WebMvcConfigurer)에서 정의한 정책을
                //      Spring Security가 읽어 와서 적용하게 된다.
                .cors(Customizer.withDefaults())

                // ────────────────────────────────────────────────
                // 2) CSRF(사이트 간 요청 위조) 보호 비활성화
                //    - 세션 기반 폼 로그인·JSP 가 아니라 REST API(JWT·토큰)만 쓴다면
                //      보통 꺼 두지만, Web → 서버 폼 전송이라면 유지해야 한다.
                .csrf(csrf -> csrf.disable())

                // ────────────────────────────────────────────────
                // 3) HTTP Basic 인증 활성화
                //    - 브라우저 팝업형 Basic Auth를 허용한다는 뜻.
                //      필요 없으면 `.httpBasic(HttpSecurity::disable)` 로 끌 수 있다.
                .httpBasic(Customizer.withDefaults())

                // ────────────────────────────────────────────────
                // 4) 인가(Authorization) 규칙
                //    - 현재는 “모든 요청 → 무조건 허용”.
                //      `/api/**` 는 인증 필요, 그 외 공개 등으로 세분화하려면
                //      .authorizeHttpRequests(auth -> auth
                //          .requestMatchers("/api/**").authenticated()
                //          .anyRequest().permitAll());
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());

        return http.build();
    }

}

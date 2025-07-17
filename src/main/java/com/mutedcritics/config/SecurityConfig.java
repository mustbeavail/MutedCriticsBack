package com.mutedcritics.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
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
                .httpBasic().disable() // HTTP Basic 인증 비활성화
                .csrf().disable() // CSRF 보호 비활성화 (SPA에서 흔히 사용)
                .sessionManagement(session -> session
                        // JWT 등을 사용할 경우 세션을 사용하지 않음 (Stateless)
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authorize -> authorize
                        // ⭐⭐ OPTIONS 요청은 모든 경로에 대해 인증 없이 허용 ⭐⭐
                        .requestMatchers(new AntPathRequestMatcher("/**", HttpMethod.OPTIONS.name())).permitAll()
                        // ⭐⭐ 로그인/회원가입 등 인증이 필요 없는 경로 허용 ⭐⭐
                        // 예시: .requestMatchers("/api/auth/**").permitAll()
                        // ⭐⭐ 특정 API 경로에 대한 접근 권한 설정 (필요시) ⭐⭐
                        // 예시: .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        // 나머지 모든 요청은 인증 필요 (로그인한 사용자만 접근 가능)
                        .anyRequest().authenticated() // 개발 시 .permitAll()로 두었다면, 이젠 .authenticated()로 변경 고려
                );

        return http.build();
    }

}

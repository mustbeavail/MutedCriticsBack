package com.mutedcritics.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.mutedcritics.utils.LoginChecker;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class InterCeptorConfig implements WebMvcConfigurer {

	private final LoginChecker checker;

	@Override
	public void addInterceptors(@NonNull InterceptorRegistry registry) {

		registry.addInterceptor(checker)
				.addPathPatterns("/**")
				.excludePathPatterns("/login*", "/join*", "/error*", "/find*", "/find_pw*", "/find_pw/send_code*");

	}

}
package com.mutedcritics;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import com.mutedcritics.utils.JwtUtil;

public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		System.out.println("키 생성");

		// pri_key가 없을 경우 생성
		if (JwtUtil.getPri_key() == null) {
			JwtUtil.setPri_key();
		}

		return application.sources(MutedCriticsApplication.class);
	}

}

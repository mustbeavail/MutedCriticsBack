package com.mutedcritics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.mutedcritics.utils.JwtUtil;

@EnableScheduling
@SpringBootApplication
public class MutedCriticsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MutedCriticsApplication.class, args);

		// pri_key가 없을 경우 생성
		if (JwtUtil.getPri_key() == null) {
			JwtUtil.setPri_key();
		}

	}

}

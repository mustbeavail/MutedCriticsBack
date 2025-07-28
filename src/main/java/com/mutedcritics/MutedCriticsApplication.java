package com.mutedcritics;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.mutedcritics.utils.JwtUtil;

@EnableScheduling
@SpringBootApplication
public class MutedCriticsApplication {

	@PostConstruct
    public void init() {
        // JVM 전역 시간대를 한국으로 설정
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
        System.setProperty("user.timezone", "Asia/Seoul");
    }
	public static void main(String[] args) {
		SpringApplication.run(MutedCriticsApplication.class, args);

		// pri_key가 없을 경우 생성
		if (JwtUtil.getPri_key() == null) {
			JwtUtil.setPri_key();
		}

	}

}

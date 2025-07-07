package com.mutedcritics.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import lombok.extern.slf4j.Slf4j;
import java.util.Map;

@Component
@Slf4j
public class LoginChecker implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		log.info("Inter Ceptor : {}", request.getRequestURI());

		boolean login = false;
		try {
			String token = request.getHeader("authorization");
			Map<String, Object> payload = JwtUtil.readToken(token);
			log.info("token : {}", token);
			String loginId = (String) payload.get("member_id");
			log.info("loginId : {}", loginId);

			if (loginId != null && !loginId.isEmpty()) {
				login = true;
			}
		} catch (Exception e) {
			log.warn("토큰이 없거나 유효하지 않습니다: {}", e.getMessage());
		}

		if (!login) {
			String ctx = request.getContextPath();
			log.info("context path : " + ctx);
			response.sendRedirect(ctx);
		}

		return login;
	}

}
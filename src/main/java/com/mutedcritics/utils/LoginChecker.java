package com.mutedcritics.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mutedcritics.entity.MemberToken;
import com.mutedcritics.member.repository.MemberTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class LoginChecker implements HandlerInterceptor {

    private final MemberTokenRepository memberTokenRepository;

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
                Optional<MemberToken> optional = memberTokenRepository.findByMemberId(loginId);
                if (optional.isPresent()) {
                    String validToken = optional.get().getToken();
                    if (validToken.equals(token)) {
                        login = true;
                    } else {
                        log.warn("동일 계정이 다른 곳에서 로그인되었습니다 : {}", loginId);
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 반환
                        response.getWriter().write("동일 계정이 다른 곳에서 로그인되었습니다.");
                        return false;
                    }
                }
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
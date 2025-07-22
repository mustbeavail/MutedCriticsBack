package com.mutedcritics.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.extern.slf4j.Slf4j;

/**
 * 전역 예외 처리 클래스입니다.
 * 컨트롤러에서 발생한 RuntimeException을 잡아 공통된 JSON 응답 형태로 반환합니다.
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * RuntimeException 예외가 발생했을 때 호출되는 핸들러 메서드입니다.
     *
     * @param e 발생한 예외 객체
     * @return 클라이언트에게 JSON 형태로 오류 메시지를 반환 (HTTP 상태코드 400)
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException e) {

        // 에러 로그 출력
        log.error("무언가 잘못되었습니다. 코드를 다시 점검해 보세요.", e);
        log.error("msg: {}", e.getMessage());

        // 응답 본문 구성
        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("msg", e.getMessage() != null ?
                e.getMessage() : "알 수 없는 오류가 발생했습니다.");

        // 400 Bad Request와 함께 JSON 응답 반환
        return ResponseEntity.badRequest().body(response);
    }
}

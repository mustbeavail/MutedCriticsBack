package com.mutedcritics.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException e) {

        log.error("무언가 잘못되었습니다. 코드를 다시 점검해 보세요.", e);
        log.error("msg: {}", e.getMessage());

        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("msg", e.getMessage() != null ?
            e.getMessage() : "알 수 없는 오류가 발생했습니다.");
        
        return ResponseEntity.badRequest().body(response);
    }
}

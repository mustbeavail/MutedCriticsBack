package com.mutedcritics.user.controller;

import com.mutedcritics.dto.UserMemoRequestDTO;
import com.mutedcritics.dto.UserMemoResponseDTO;
import com.mutedcritics.entity.Response;
import com.mutedcritics.user.service.UserMemoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class UserMemoController {
    private final UserMemoService service;

    // 유저 메모 작성
    @PostMapping("/user/write/memo")
    public ResponseEntity<?> writeMemo(@RequestBody UserMemoRequestDTO request) {
        log.info("유저 메모 작성 요청 : {}, {}, {}", request.getMemberId(), request.getUserId(), request.getMemo());
        service.writeMemo(request);
        return ResponseEntity.ok(Map.of("msg", "메모 작성 완료"));
    }

    // 메모 리스트 조회
    @GetMapping("/user/{userId}/list")
    public ResponseEntity<?> getUserMemos(@PathVariable String userId) {
        log.info("메모 리스트 조회 요청 : {}", userId);
        return ResponseEntity.ok(service.getMemosByUser(userId));
    }

    // 유저 메모 수정 페이지
    @GetMapping("/user/{memoIdx}/update-page")
    public ResponseEntity<?> updateMemoPage(@PathVariable int memoIdx, @RequestBody UserMemoRequestDTO request) {
        log.info("메모 수정 페이지 요청 : {}, {}", memoIdx, request.getMemberId());
        UserMemoResponseDTO memo = service.updateMemoPage(memoIdx, request);
        return ResponseEntity.ok(memo);
    }

    // 유저 메모 수정
    @PutMapping("/user/{memoIdx}/update")
    public ResponseEntity<?> updateMemo(@PathVariable int memoIdx, @RequestBody UserMemoRequestDTO request) {
        log.info("메모 수정 요청 : {}, {}, {}", memoIdx, request.getMemberId(), request.getMemo());
        service.updateMemo(memoIdx, request);
        return ResponseEntity.ok(Map.of("msg", "메모 수정 완료"));
    }

    // 유저 메모 삭제
    @DeleteMapping("/user/{memoIdx}/delete")
    public ResponseEntity<?> deleteMemo(@PathVariable int memoIdx, @RequestBody Map<String, Object> request) {
        log.info("메모 삭제 요청 : {}, {}", memoIdx, request.get("memberId"));
        service.deleteMemo(memoIdx, request);
        return ResponseEntity.ok(Map.of("msg", "메모 삭제 완료"));
    }
}

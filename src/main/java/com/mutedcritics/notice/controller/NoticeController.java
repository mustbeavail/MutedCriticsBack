package com.mutedcritics.notice.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mutedcritics.notice.service.NoticeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RestController
@Slf4j
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService service;
    Map<String, Object> resp = null;

    // 알림 읽음 처리
    @PutMapping("/notice/read")
    public ResponseEntity<String> readNotice(@RequestBody int notiIdx) {

        boolean success = service.readNotice(notiIdx);

        if (success) {
            return ResponseEntity.ok("알림 읽음 처리 완료");
        }
        else {
            return ResponseEntity.badRequest().body("알림 읽음 처리 실패");
        }
    }

    // 채팅 알림 목록 조회
    @GetMapping("/notice/chat/list")
    public Map<String, Object> getChatNoticeList(
        @RequestParam String memberId){

        resp = new HashMap<>();
        List<Map<String, Object>> notiList = service.getChatNoticeList(memberId);

        resp.put("notiList", notiList);

        return resp;
    }

    // 통계 알림 목록 조회
    @GetMapping("/notice/stat/list")
    public Map<String, Object> getStatNoticeList(){

        resp = new HashMap<>();

        List<Map<String, Object>> notiList = service.getStatNoticeList();

        resp.put("notiList", notiList);

        return resp;
    }

}

package com.mutedcritics.item.controller;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mutedcritics.item.service.ItemHistoricalService;
import com.mutedcritics.item.service.ItemService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RestController
@Slf4j
@RequiredArgsConstructor
public class ItemController {

    private final ItemService service;
    private final ItemHistoricalService historicalService;

    // 이전 통계 일괄 저장
    @PostMapping("/item/historical")
    public ResponseEntity<String> calculateHistorical(
            @RequestParam String startDate,
            @RequestParam String endDate) {

        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);

            // 비동기로 처리
            CompletableFuture.runAsync(() -> historicalService.processHistoricalStats(start, end));

            return ResponseEntity.ok("통계 계산이 시작되었습니다.");

        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest()
                    .body("날짜 형식이 잘못되었습니다. YYYY-MM-DD 형식을 사용해주세요.");
        }
    }

    // 아이템 리스트 조회, 검색
    @GetMapping("/item/list")
    public Map<String, Object> itemList(
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam String align,
            @RequestParam Integer page,
            @RequestParam(required = false) String search) {

        Map<String, Object> resp = new HashMap<>();

        List<Map<String, Object>> list = service.itemList(startDate, endDate, align, page, search);
        resp.put("itemList", list);

        return resp;
    }

    // 이벤트 아이템 조회
    @GetMapping("/item/event")
    public Map<String, Object> eventItemList(
            @RequestParam String eventName,
            @RequestParam String align) {

        Map<String, Object> resp = new HashMap<>();

        List<Map<String, Object>> list = service.eventItemList(eventName, align);
        resp.put("eventItemList", list);

        return resp;
    }

    // 아이템 환불내역 리스트 조회 / 검색
    @GetMapping("/item/refund/list")
    public Map<String, Object> refundList(
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam String align,
            @RequestParam int page,
            @RequestParam(required = false) String search) {

        Map<String, Object> resp = new HashMap<>();

        List<Map<String, Object>> list = service.refundList(startDate, endDate, align, page, search);

        resp.put("refundList", list);

        return resp;
    }

    // 아이템 환불내역 요약 조회
    @GetMapping("/item/refund/summary")
    public Map<String, Object> refundSummary(
            @RequestParam String startDate,
            @RequestParam String endDate) {

        Map<String, Object> resp = new HashMap<>();

        List<Map<String, Object>> list = service.refundSummary(startDate, endDate);

        resp.put("refundSummary", list);

        return resp;
    }
}

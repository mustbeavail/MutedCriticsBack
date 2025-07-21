package com.mutedcritics.ingame.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mutedcritics.dto.HeroBanPickRateDTO;
import com.mutedcritics.dto.HeroItemCountDTO;
import com.mutedcritics.dto.HeroPlayTimeDTO;
import com.mutedcritics.dto.HeroPotgRateDTO;
import com.mutedcritics.dto.HeroWinRateDTO;
import com.mutedcritics.dto.ModePlayTimeDTO;
import com.mutedcritics.ingame.service.IngameService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RestController
@Slf4j
@RequiredArgsConstructor
public class IngameController {

    private final IngameService service;

    /**
     * 일일 영웅 통계 업데이트
     *
     * @return 일일 통계 업데이트 문구 출력
     */
    @PostMapping("/insert/daily-hero-stats")
    public ResponseEntity<?> insertDailyHeroStats(@RequestBody Map<String, Object> param) {
        LocalDate startDate = LocalDate.parse(param.get("startDate").toString());

        // 종료일이 없으면 시작일로 설정(요청 바디 하나로 들어올 때)
        LocalDate endDate;
        if (param.containsKey("endDate")) {
            endDate = LocalDate.parse(param.get("endDate").toString());
        } else {
            endDate = startDate;
        }

        log.info("startDate : {}, endDate : {}", startDate, endDate);
        service.insertDailyHeroStats(startDate, endDate);
        return ResponseEntity.ok(Map.of("msg", startDate + " ~ " + endDate + " 날짜의 일일 영웅 통계가 업데이트 되었습니다."));
    }

    /**
     * 영웅이 보유한 아이템 수
     *
     * @param sortOrder
     * @return
     */
    @GetMapping("/get/hero-item-count")
    public ResponseEntity<?> getHeroItemCount(@RequestParam(defaultValue = "DESC") String sortOrder) {
        List<HeroItemCountDTO> list = service.getHeroItemCount(sortOrder);
        return ResponseEntity.ok(Map.of("list", list));
    }

    /**
     * 영웅별 총 플레이타임, potg, 아이템 개수
     *
     * @param sortOrder
     * @param sortBy
     * @return
     */
    @GetMapping("/get/hero-playtime-potg-item-count")
    public ResponseEntity<?> getHeroPlaytimePotgItemCount(
            @RequestParam(defaultValue = "DESC") String sortOrder,
            @RequestParam(defaultValue = "totalPlayTime") String sortBy) {
        List<Map<String, Object>> list = service.getHeroPlaytimePotgItemCount(sortOrder, sortBy);
        return ResponseEntity.ok(Map.of("list", list));
    }

    /**
     * 영웅별 전체 플레이타임 조회
     * 플레이타임 높은순, 낮은순으로 정렬 가능
     *
     * @param sortOrder DESC(높은순) 또는 ASC(낮은순)
     * @return
     */
    @GetMapping("/get/hero-playtime")
    public ResponseEntity<?> getHeroPlayTime(@RequestParam(defaultValue = "DESC") String sortOrder) {
        List<HeroPlayTimeDTO> list = service.getHeroPlayTime(sortOrder);
        return ResponseEntity.ok(Map.of("list", list));
    }

    /**
     * 모드별 전체 플레이타임 조회
     * 플레이타임 높은순, 낮은순으로 정렬 가능
     *
     * @param sortOrder DESC(높은순) 또는 ASC(낮은순)
     * @return
     */
    @GetMapping("/get/mode-playtime")
    public ResponseEntity<?> getModePlayTime(@RequestParam(defaultValue = "DESC") String sortOrder) {
        List<ModePlayTimeDTO> list = service.getModePlayTime(sortOrder);
        return ResponseEntity.ok(Map.of("list", list));
    }

    /**
     * 영웅별 승률 조회
     * 높은순, 낮은순 정렬 가능
     * 기간 설정 가능
     * 티어별 승률 보기 가능
     *
     * @param startDate 시작일 (선택사항)
     * @param endDate   종료일 (선택사항)
     * @param tierName  티어명 (선택사항)
     * @param sortOrder DESC(높은순) 또는 ASC(낮은순)
     * @return
     */
    @GetMapping("/get/hero-winrate")
    public ResponseEntity<?> getHeroWinRate(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String tierName,
            @RequestParam(defaultValue = "DESC") String sortOrder) {

        LocalDate start = startDate != null ? LocalDate.parse(startDate) : null;
        LocalDate end = endDate != null ? LocalDate.parse(endDate) : null;

        List<HeroWinRateDTO> list = service.getHeroWinRate(start, end, tierName, sortOrder);
        return ResponseEntity.ok(Map.of("list", list));
    }

    /**
     * 영웅별 최고의 플레이 비중 조회
     * 영웅별 출전 횟수와 최고의 플레이 수, 출전 횟수 대비 최고의 플레이비율을 보여주는 기능
     * 높은순, 낮은순 정렬 가능
     *
     * @param startDate 시작일 (선택사항)
     * @param endDate   종료일 (선택사항)
     * @param sortOrder DESC(높은순) 또는 ASC(낮은순)
     * @return
     */
    @GetMapping("/get/hero-potg-rate")
    public ResponseEntity<?> getHeroPotgRate(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "DESC") String sortOrder) {

        LocalDate start = startDate != null ? LocalDate.parse(startDate) : null;
        LocalDate end = endDate != null ? LocalDate.parse(endDate) : null;

        List<HeroPotgRateDTO> list = service.getHeroPotgRate(start, end, sortOrder);
        return ResponseEntity.ok(Map.of("list", list));
    }

    /**
     * 영웅별 밴률/픽률 조회
     * 전체 게임 횟수 대비 영웅의 밴 횟수와 픽 횟수 비율을 보여주는 기능
     * 높은순, 낮은순 정렬 가능
     * 기간 설정 가능
     *
     * @param startDate 시작일 (선택사항)
     * @param endDate   종료일 (선택사항)
     * @param sortOrder DESC(높은순) 또는 ASC(낮은순)
     * @return
     */
    @GetMapping("/get/hero-banpick-rate")
    public ResponseEntity<?> getHeroBanPickRate(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "DESC") String sortOrder) {

        LocalDate start = startDate != null ? LocalDate.parse(startDate) : null;
        LocalDate end = endDate != null ? LocalDate.parse(endDate) : null;

        List<HeroBanPickRateDTO> list = service.getHeroBanPickRate(start, end, sortOrder);
        return ResponseEntity.ok(Map.of("list", list));
    }

    /**
     * 새벽 1시마다 일일 영웅 통계 업데이트
     * 어제 날짜로 업데이트
     */
    @Scheduled(cron = "0 0 1 * * *")
    public void insertDailyHeroStats() {
        service.insertDailyHeroStats(LocalDate.now().minusDays(1), LocalDate.now().minusDays(1));
    }
}

package com.mutedcritics.item.component;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ItemScheduler {

    @Scheduled(cron = "0 0 0 * * *")
    public void itemStats() {
        log.info("일일 아이템 통계 시작");
    }
}

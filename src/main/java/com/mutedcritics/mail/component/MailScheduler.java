package com.mutedcritics.mail.component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mutedcritics.entity.AutoSend;
import com.mutedcritics.mail.repository.AutoSendRepository;
import com.mutedcritics.mail.service.MailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@EnableScheduling
@Slf4j
@RequiredArgsConstructor
public class MailScheduler {

    private final AutoSendRepository autoSendRepo;
    private final MailService mailService;

    // 정기 메일 발송
    @Scheduled(cron = "0 0 9 * * ?")
    public void sendScheduledMails() {
        LocalDate today = LocalDate.now();
        List<AutoSend> scheduledMails = autoSendRepo.findScheduledMails(today);

        log.info("정기 메일 발송 메서드 실행");
        
        if (scheduledMails.isEmpty() || scheduledMails == null) {
            log.info("오늘 발송할 정기메일이 없습니다.");
            return;
        }

        for (AutoSend autoSend : scheduledMails) {
            // 메일 발송
            Map<String, Object> params = new HashMap<>();
            params.put("temIdx", autoSend.getMailTemplate().getTemIdx());
            params.put("recipient", autoSend.getRecipient());
            params.put("mailSub", autoSend.getMailSub());
            params.put("mailContent", autoSend.getMailContent());
            params.put("isToAll", autoSend.isToAll());
            params.put("memberId", autoSend.getMember().getMemberId());
            params.put("intervalDays", autoSend.getIntervalDays());
            params.put("isActive", autoSend.isActive());
            params.put("nextSendDate", autoSend.getNextSendDate());
            
            boolean success = mailService.sendMailInterval(params);
            
            if (success) {
                if (autoSend.getIntervalDays() > 0) {
                    // 반복 예약
                    LocalDate nextDate = today.plusDays(autoSend.getIntervalDays());
                    autoSend.setNextSendDate(nextDate);
                    autoSendRepo.save(autoSend);
                } else {
                    // 단발성 예약: 발송 후 비활성화
                    autoSend.setActive(false);
                    autoSendRepo.save(autoSend);
                }
            }
        }
    }
}

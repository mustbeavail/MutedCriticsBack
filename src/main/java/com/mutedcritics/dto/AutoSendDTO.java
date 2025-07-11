package com.mutedcritics.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.mutedcritics.entity.AutoSend;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AutoSendDTO {

    private int scheduleIdx;
    private LocalDateTime createdAt;
    private int intervalDays;
    private boolean isActive;
    private boolean isToAll;
    private LocalDate nextSendDate;
    private String recipient;
    private int temIdx;
    private String mailContent;
    private String mailSub;
    private String memberId;

    public AutoSendDTO(AutoSend autoSend) {
        this.scheduleIdx = autoSend.getScheduleIdx();
        this.createdAt = autoSend.getCreatedAt();
        this.intervalDays = autoSend.getIntervalDays();
        this.isActive = autoSend.isActive();
        this.isToAll = autoSend.isToAll();
        this.nextSendDate = autoSend.getNextSendDate();   
        this.recipient = autoSend.getRecipient();
        this.temIdx = autoSend.getMailTemplate().getTemIdx();
        this.mailContent = autoSend.getMailContent();
        this.mailSub = autoSend.getMailSub();
        this.memberId = autoSend.getMember().getMemberId();
    }
}

package com.mutedcritics.dto;

import java.time.LocalDateTime;

import com.mutedcritics.entity.Mail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MailDTO {

    private int mailIdx;
    private boolean isToAll;
    private String mailContent;
    private LocalDateTime mailDate;
    private String mailSub;
    private String recipient;
    private int temIdx;
    private String memberId;

    public MailDTO(Mail mail) {
        this.mailIdx = mail.getMailIdx();
        this.isToAll = mail.isToAll();
        this.mailContent = mail.getMailContent();
        this.mailDate = mail.getMailDate();
        this.mailSub = mail.getMailSub();
        this.recipient = mail.getRecipient();
        this.temIdx = mail.getMailTemplate().getTemIdx();
        this.memberId = mail.getMember().getMemberId();
    }   

}

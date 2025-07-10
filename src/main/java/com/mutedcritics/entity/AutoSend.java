package com.mutedcritics.entity;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "auto_send")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutoSend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_idx")
    private int scheduleIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tem_idx")
    private MailTemplate mailTemplate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "is_to_all")
    private boolean isToAll;

    @Column(name = "recipient", columnDefinition = "text")
    private String recipient;

    @Column(name = "mail_sub", length = 255)
    private String mailSub;

    @Lob
    @Column(name = "mail_content", columnDefinition = "longtext")
    private String mailContent;

    @Column(name = "interval_days")
    private int intervalDays;

    @Column(name = "next_send_date")
    private LocalDate nextSendDate;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

}
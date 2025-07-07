package com.mutedcritics.entity;

import javax.persistence.*;
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

    @Column(name = "tem_idx")
    private int temIdx;

    @Column(name = "is_to_all")
    private boolean isToAll;

    @Column(name = "recipient", columnDefinition = "text")
    private String recipient;

    @Column(name = "interval_days")
    private int intervalDays;

    @Column(name = "next_send_date")
    private LocalDate nextSendDate;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
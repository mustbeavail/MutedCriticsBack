package com.mutedcritics.entity;

import javax.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "stats_inquiry_daily", uniqueConstraints = @UniqueConstraint(columnNames = { "stats_date", "ticket_type",
        "category" }))
@Data
public class StatsInquiryDaily {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inquiry_stats_id")
    private Long inquiryStatsId;

    @Column(name = "stats_date", nullable = false)
    private LocalDate statsDate;

    @Column(name = "ticket_type", columnDefinition = "char(2)")
    private String ticketType;

    @Column(name = "category", length = 50)
    private String category;

    @Column(name = "daily_ticket_count")
    private int dailyTicketCount;

    @Column(name = "unresolved_total")
    private int unresolvedTotal;

    @Column(name = "vip_ticket_count")
    private int vipTicketCount;
}
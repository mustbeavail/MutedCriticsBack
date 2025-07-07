package com.mutedcritics.entity;

import javax.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "stats_revenue_daily")
@Data
public class StatsRevenueDaily {

    @Id
    @Column(name = "stats_date")
    private LocalDate statsDate;

    @Column(name = "daily_revenue")
    private Long dailyRevenue;

    @Column(name = "daily_purchase_count")
    private int dailyPurchaseCount;

    @Column(name = "daily_paying_users")
    private int dailyPayingUsers;

    @Column(name = "arpu")
    private Long arpu;

    @Column(name = "arppu")
    private Long arppu;

    @Column(name = "daily_interval")
    private int dailyInterval;
}
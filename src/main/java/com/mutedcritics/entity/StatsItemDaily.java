package com.mutedcritics.entity;

import javax.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "stats_item_daily", uniqueConstraints = @UniqueConstraint(columnNames = { "stats_date", "item_idx" }))
@Data
public class StatsItemDaily {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_stats_id")
    private Long itemStatsId;

    @Column(name = "stats_date", nullable = false)
    private LocalDate statsDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_idx")
    private ItemList itemList;

    @Column(name = "daily_sales_revenue")
    private Long dailySalesRevenue;

    @Column(name = "daily_sales_count")
    private int dailySalesCount;

    @Column(name = "daily_paying_users")
    private int dailyPayingUsers;

    @Column(name = "daily_refund_amount")
    private Long dailyRefundAmount;

    @Column(name = "daily_refund_count")
    private int dailyRefundCount;

    @Column(name = "returning_user_buy_count")
    private int returningUserBuyCount;

    @Column(name = "regular_user_buy_count")
    private int regularUserBuyCount;

    @Column(name = "vip_user_buy_count")
    private int vipUserBuyCount;
}
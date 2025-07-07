package com.mutedcritics.entity;

import javax.persistence.*;

import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "stats_hero_daily", uniqueConstraints = @UniqueConstraint(columnNames = { "stats_date", "heroes_idx",
        "tier_name" }))
@Data
public class StatsHeroDaily {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hero_stats_id")
    private Long heroStatsId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "heroes_idx")
    private Heroes heroes;

    @Column(name = "stats_date", nullable = false)
    private LocalDate statsDate;

    @Column(name = "tier_name", length = 20)
    private String tierName;

    @Column(name = "win_count")
    private int winCount;

    @Column(name = "lose_count")
    private int loseCount;

    @Column(name = "pick_count")
    private int pickCount;

    @Column(name = "ban_count")
    private int banCount;

    @Column(name = "potg_count")
    private int potgCount;

    @Column(name = "total_play_hours")
    private int totalPlayHours;
}
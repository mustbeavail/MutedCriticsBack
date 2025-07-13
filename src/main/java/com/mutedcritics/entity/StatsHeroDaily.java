package com.mutedcritics.entity;

import javax.persistence.*;

import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "stats_hero_daily", uniqueConstraints = @UniqueConstraint(columnNames = { "stats_date", "heroes_idx",
        "match_mode" }))
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

    @Column(name = "match_mode", length = 50, nullable = false)
    private String matchMode;

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

    @Column(name = "total_play_time")
    private int totalPlayTime;
}
package com.mutedcritics.entity;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "match_heroes")
public class MatchHeroes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_heroes_idx")
    private int matchHeroesIdx;

    // 외래키(match 테이블의 match_idx)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_idx")
    private Match match;

    // 외래키(heroes 테이블의 heroes_idx)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "heroes_idx")
    private Heroes heroes;

    @Column(name = "pick_or_ban", length = 10)
    private String pickOrBan;

    @Column(name = "attack_or_defense", length = 10)
    private String attackOrDefense;

    @Column(name = "potg")
    private boolean potg;

    // 시즌 연결 (ManyToOne)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "season_idx") // FK 컬럼
    private Season season;
}

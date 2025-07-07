package com.mutedcritics.entity;

import javax.persistence.Column;
import javax.persistence.FetchType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "match_result")
public class MatchResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "result_idx")
    private int resultIdx;

    @Column(name = "victory_or_defeat", length = 10)
    private String victoryOrDefeat;

    @Column(name = "match_play_time")
    private int matchPlayTime;

    @Column(name = "potg")
    private boolean potg;

    // 외래키(heroes 테이블의 heroes_idx)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "heroes_idx")
    private Heroes heroes;

    // 외래키(match 테이블의 match_idx)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_idx")
    private Match match;

    // 외래키(user 테이블의 user_id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}

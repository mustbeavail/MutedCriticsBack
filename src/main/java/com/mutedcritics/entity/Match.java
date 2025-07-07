package com.mutedcritics.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "match_table")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_idx")
    private int matchIdx;

    @Column(name = "match_start_date")
    private LocalDateTime matchStartDate;

    @Column(name = "match_end_date")
    private LocalDateTime matchEndDate;

    @Column(name = "match_date")
    private LocalDate matchDate;

    @Column(name = "match_mode", length = 50)
    private String matchMode;
}

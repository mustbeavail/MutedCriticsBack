package com.mutedcritics.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "season")
public class Season {

    @Id
    @Column(name = "season_idx")
    private Integer seasonIdx;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "active_yn")
    private Boolean activeYn;

}

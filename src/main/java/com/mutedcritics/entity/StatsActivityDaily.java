package com.mutedcritics.entity;

import javax.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "stats_activity_daily")
@Data
public class StatsActivityDaily {

    @Id
    @Column(name = "stats_date")
    private LocalDate statsDate;

    @Column(name = "dau")
    private int dau;

    @Column(name = "new_user_count")
    private int newUserCount;

    @Column(name = "dormant_user_count")
    private int dormantUserCount;

    @Column(name = "returning_user_count")
    private int returningUserCount;

    @Column(name = "withdrawn_user_count")
    private int withdrawnUserCount;

}
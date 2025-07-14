package com.mutedcritics.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "user")
public class User {

    @Id
    @Column(name = "user_id", length = 50)
    private String userId;

    @Column(name = "user_pw", length = 50)
    private String userPw;

    @Column(name = "user_nick", length = 50)
    private String userNick;

    @Column(name = "user_gender", length = 10)
    private String userGender;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "region", length = 20)
    private String region;

    @Column(name = "join_date")
    private LocalDate joinDate;

    @Column(name = "dormant_date")
    private LocalDate dormantDate;

    @Column(name = "returning_date")
    private LocalDate returningDate;

    @Column(name = "withdraw_date")
    private LocalDate withdrawDate;

    @Column(name = "user_type")
    private String userType;

    @Column(name = "vip_yn")
    private boolean vipYn;

    @Column(name = "receive_yn")
    private boolean receiveYn;

    @Column(name = "receive_yn_at")
    private LocalDate receiveYnAt;

}

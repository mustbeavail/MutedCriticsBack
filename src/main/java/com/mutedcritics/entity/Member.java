package com.mutedcritics.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;

@Entity
@Data
@Table(name = "member")
public class Member {

    @Id
    @Column(name = "member_id", length = 50)
    private String memberId;

    @Column(name = "member_pw", length = 255)
    private String memberPw;

    @Column(name = "member_name", length = 50)
    private String memberName;

    @Column(name = "member_gender", length = 10)
    private String memberGender;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "office_phone", length = 20)
    private String officePhone;

    @Column(name = "mobile_phone", length = 20)
    private String mobilePhone;

    @Column(name = "position", length = 20)
    private String position;

    @Column(name = "dept_name", length = 20)
    private String deptName;

    @CreationTimestamp
    @Column(name = "join_date")
    private LocalDate joinDate;

    @Column(name = "withdraw_date")
    private LocalDate withdrawDate;

    @Column(name = "admin_yn")
    private boolean adminYn;

    @Column(name = "accept_yn")
    private boolean acceptYn = false;
}

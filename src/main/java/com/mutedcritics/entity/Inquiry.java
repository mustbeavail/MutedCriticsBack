package com.mutedcritics.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;

@Entity
@Data
@Table(name = "inquiry")
public class Inquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inquiry_idx")
    private int inquiryIdx;

    // 외래키(user 테이블의 user_id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 외래키(user 테이블의 user_id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_id")
    private User reportedUser;

    @Column(name = "category", length = 50)
    private String category;

    @Column(name = "title", length = 50)
    private String title;

    @Lob
    @Column(name = "content", columnDefinition = "longtext")
    private String content;

    @Column(name = "status", length = 10)
    private String status;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "agent_res_yn")
    private boolean agentResYn;
}

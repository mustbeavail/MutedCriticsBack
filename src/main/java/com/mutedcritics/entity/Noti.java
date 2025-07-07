package com.mutedcritics.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;

@Entity
@Data
@Table(name = "noti")
public class Noti {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "noti_idx")
    private int notiIdx;

    @Column(name = "content_pre", length = 500)
    private String contentPre;

    @Column(name = "related_idx")
    private int relatedIdx;

    @Column(name = "read_yn")
    private boolean readYn;

    @Column(name = "noti_type", length = 20)
    private String notiType;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // 외래키(user 테이블의 user_id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}

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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;
import lombok.ToString;

@Entity
@Data
@Table(name = "response")
public class Response {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "response_idx")
    private int responseIdx;

    // 외래키(inquiry 테이블의 inquiry_idx)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inquiry_idx")
    @ToString.Exclude
    private Inquiry inquiry;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id")
    private Member agent;

    @Lob
    @Column(name = "content", columnDefinition = "longtext")
    private String content;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

}

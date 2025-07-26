package com.mutedcritics.entity;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
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

    @Column(name = "type", columnDefinition = "char(2)")
    private String type;

    @Column(name = "category", length = 50)
    private String category;

    @Column(name = "title", length = 50)
    private String title;

    @Lob
    @Column(name = "content", columnDefinition = "longtext")
    private String content;

    @Column(name = "status", length = 6)
    private String status;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    // 응답 목록
    @OneToOne(mappedBy = "inquiry", cascade = CascadeType.ALL)
    @ToString.Exclude // 롬복 @Data 사용시, 양방향 매핑 시 무한 루프 방지
    private Response response;

}

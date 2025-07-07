package com.mutedcritics.entity;

import java.time.LocalDateTime;

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
@Table(name = "forum_comment")
public class ForumComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "com_idx")
    private int comIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_idx")
    private ForumPost forumPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "com_content", columnDefinition = "longtext")
    private String comContent;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}

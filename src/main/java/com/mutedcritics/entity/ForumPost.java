package com.mutedcritics.entity;

import java.time.LocalDateTime;

import javax.persistence.FetchType;
import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "forum_post")
public class ForumPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_idx")
    private int postIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "topic", length = 50)
    private String topic;

    @Column(name = "title", length = 50)
    private String title;

    @Lob
    @Column(name = "content", columnDefinition = "longtext")
    private String content;

    @Column(name = "hit")
    private int hit;

    @Column(name = "likes")
    private int likes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

package com.mutedcritics.dto;

import java.time.LocalDateTime;

import com.mutedcritics.entity.ForumPost;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForumPostDTO {

    private int postIdx;
    private String content;
    private LocalDateTime createdAt;
    private int hit;
    private int likes;
    private String title;
    private String topic;
    private LocalDateTime updatedAt;
    private String userId;

    public ForumPostDTO(ForumPost post) {
        this.postIdx = post.getPostIdx();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.hit = post.getHit();
        this.likes = post.getLikes();
        this.title = post.getTitle();
        this.topic = post.getTopic();
        this.updatedAt = post.getUpdatedAt();
        this.userId = post.getUser().getUserId();
    }
}

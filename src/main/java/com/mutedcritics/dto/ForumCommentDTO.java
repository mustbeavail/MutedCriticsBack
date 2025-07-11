package com.mutedcritics.dto;

import java.time.LocalDateTime;

import com.mutedcritics.entity.ForumComment;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForumCommentDTO {

    private int comIdx;
    private String comContent;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int postIdx;
    private String userId;

    public ForumCommentDTO(ForumComment comment) {
        this.comIdx = comment.getComIdx();
        this.comContent = comment.getComContent();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
        this.postIdx = comment.getForumPost().getPostIdx();
        this.userId = comment.getUser().getUserId();
    }
}

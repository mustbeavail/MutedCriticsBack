package com.mutedcritics.forum.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mutedcritics.entity.ForumComment;

public interface ForumCommentRepository extends JpaRepository<ForumComment, Integer> {

    @Query("SELECT c FROM ForumComment c WHERE c.forumPost.postIdx = :postIdx AND c.createdAt <= :today")
    Page<ForumComment> findByForumPostPostIdx(@Param("postIdx") int postIdx, Pageable pageable, @Param("today") LocalDateTime today);

}

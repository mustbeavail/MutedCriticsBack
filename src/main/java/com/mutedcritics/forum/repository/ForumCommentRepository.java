package com.mutedcritics.forum.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mutedcritics.entity.ForumComment;

public interface ForumCommentRepository extends JpaRepository<ForumComment, Integer> {

}

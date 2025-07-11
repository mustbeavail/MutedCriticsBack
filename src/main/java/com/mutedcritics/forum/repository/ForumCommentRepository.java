package com.mutedcritics.forum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mutedcritics.entity.ForumComment;

public interface ForumCommentRepository extends JpaRepository<ForumComment, Integer> {

    Page<ForumComment> findByForumPostPostIdx(int postIdx, Pageable pageable);

}

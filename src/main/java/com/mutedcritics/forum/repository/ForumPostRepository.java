package com.mutedcritics.forum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mutedcritics.entity.ForumPost;

public interface ForumPostRepository extends JpaRepository<ForumPost, Integer> {

    Page<ForumPost> findByTopic(String topic, Pageable pageable);

}

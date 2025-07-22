package com.mutedcritics.forum.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mutedcritics.entity.ForumPost;

public interface ForumPostRepository extends JpaRepository<ForumPost, Integer> {

    @Query("SELECT p FROM ForumPost p WHERE p.topic = :topic AND p.createdAt <= :today")
    Page<ForumPost> findByTopic(String topic, Pageable pageable, LocalDateTime today);

    @Query("SELECT p FROM ForumPost p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :search, '%')) AND p.topic = :topic AND p.createdAt <= :today")
    Page<ForumPost> findByTitleContaining(@Param("search") String search, @Param("topic") String topic, Pageable pageable, @Param("today") LocalDateTime today);

    @Query("SELECT p FROM ForumPost p WHERE LOWER(p.content) LIKE LOWER(CONCAT('%', :search, '%')) AND p.topic = :topic AND p.createdAt <= :today")
    Page<ForumPost> findByContentContaining(@Param("search") String search, @Param("topic") String topic, Pageable pageable, @Param("today") LocalDateTime today);

    @Query("SELECT p FROM ForumPost p WHERE LOWER(p.user.userId) LIKE LOWER(CONCAT('%', :search, '%')) AND p.topic = :topic AND p.createdAt <= :today")
    Page<ForumPost> findByUserIdContaining(@Param("search") String search, @Param("topic") String topic, Pageable pageable, @Param("today") LocalDateTime today);

}

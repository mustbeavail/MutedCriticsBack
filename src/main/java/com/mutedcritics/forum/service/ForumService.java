package com.mutedcritics.forum.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.mutedcritics.entity.ForumPost;
import com.mutedcritics.forum.repository.ForumPostRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ForumService {

    private final ForumPostRepository forumPostRepo;
    Map<String, Object> resp = null;

    // 포럼 게시글, 댓글 불러오기
    public Page<ForumPost> getForumList(int page, String topic, String align) {

        Pageable pageable = null;
        Page<ForumPost> forumPosts = null;

        // 날짜 내림차순
        if ("dateDesc".equals(align)) {
            pageable = PageRequest.of(page - 1, 15, Sort.by("createdAt").descending());
        // 날짜 오름차순
        } else if ("dateAsc".equals(align)) {
            pageable = PageRequest.of(page - 1, 15, Sort.by("createdAt").ascending());
        // 조회수 내림차순
        } else if ("hitDesc".equals(align)) {
            pageable = PageRequest.of(page - 1, 15, Sort.by("hit").descending());
        // 조회수 오름차순
        } else if ("hitAsc".equals(align)) {
            pageable = PageRequest.of(page - 1, 15, Sort.by("hit").ascending());
        // 좋아요 내림차순
        } else if ("likesDesc".equals(align)) {
            pageable = PageRequest.of(page - 1, 15, Sort.by("likes").descending());
        // 좋아요 오름차순
        } else if ("likesAsc".equals(align)) {
            pageable = PageRequest.of(page - 1, 15, Sort.by("likes").ascending());
        }

        if ("일반".equals(topic)) {
            forumPosts = forumPostRepo.findByTopic("일반", pageable);
        } else if ("경쟁전".equals(topic)) {
            forumPosts = forumPostRepo.findByTopic("경쟁전", pageable);
        }

        if (forumPosts == null || forumPosts.isEmpty()) {
            return Page.empty();
        }

        return forumPosts;
    }
}

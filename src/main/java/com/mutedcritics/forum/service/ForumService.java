package com.mutedcritics.forum.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.mutedcritics.entity.ForumComment;
import com.mutedcritics.entity.ForumPost;
import com.mutedcritics.forum.repository.ForumCommentRepository;
import com.mutedcritics.forum.repository.ForumPostRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ForumService {

    private final ForumPostRepository forumPostRepo;
    private final ForumCommentRepository forumCommentRepo;

    // 포럼 게시글, 댓글 불러오기
    public Page<ForumPost> getForumList(int page, String topic, String align, LocalDateTime today) {

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
            forumPosts = forumPostRepo.findByTopic("일반", pageable, today);
        } else if ("경쟁전".equals(topic)) {
            forumPosts = forumPostRepo.findByTopic("경쟁전", pageable, today);
        }

        if (forumPosts == null || forumPosts.isEmpty()) {
            return Page.empty();
        }

        return forumPosts;
    }

    // 포럼 게시글 상세보기
    public ForumPost getForumPostDetail(int postIdx) {

        ForumPost forumPost = forumPostRepo.findById(postIdx)
            .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다: " + postIdx));

        return forumPost;
    }

    // 포럼 댓글 불러오기
    public Page<ForumComment> getForumComments(int postIdx, int page, LocalDateTime today) {

        Pageable pageable = PageRequest.of(page - 1, 15, Sort.by("createdAt").descending());
        Page<ForumComment> forumComments = forumCommentRepo.findByForumPostPostIdx(postIdx, pageable, today);

        if (forumComments == null || forumComments.isEmpty()) {
            forumComments = Page.empty();
        }

        return forumComments;
    }

    // 포럼 게시글 검색하기
    public Page<ForumPost> searchForumPosts(String search, String searchType, int page, String topic, LocalDateTime today) {

        if (search == null || search.trim().isEmpty()) {
            return Page.empty();
        }

        Pageable pageable = PageRequest.of(page - 1, 15, Sort.by("createdAt").descending());
        Page<ForumPost> forumPosts = null;

        if("title".equals(searchType)) {
            forumPosts = forumPostRepo.findByTitleContaining(search, topic, pageable, today);
        } else if("content".equals(searchType)) {
            forumPosts = forumPostRepo.findByContentContaining(search, topic, pageable, today);
        } else if("userId".equals(searchType)) {
            forumPosts = forumPostRepo.findByUserIdContaining(search, topic, pageable, today);
        }

        if (forumPosts == null || forumPosts.isEmpty()) {
            forumPosts = Page.empty();
        }

        return forumPosts;
    }
}

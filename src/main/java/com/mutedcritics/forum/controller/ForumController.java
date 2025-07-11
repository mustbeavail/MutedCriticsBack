package com.mutedcritics.forum.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mutedcritics.dto.ForumPostDTO;
import com.mutedcritics.entity.ForumPost;
import com.mutedcritics.forum.service.ForumService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin
@RestController
@Slf4j
@RequiredArgsConstructor
public class ForumController {

    private final ForumService service;

    Map<String, Object> resp = null;


    // 포럼 게시글 불러오기
    @GetMapping("/forum/list")
    public Map<String, Object> getForumList(
        @RequestParam int page,
        @RequestParam String topic,
        @RequestParam String align
        ) {
        resp = new HashMap<>();

        if (page > 0 &&
            ("경쟁전".equals(topic) || "일반".equals(topic)) &&
            ("dateDesc".equals(align) || "dateAsc".equals(align) ||
            "hitDesc".equals(align) || "hitAsc".equals(align) ||
            "likesDesc".equals(align) || "likesAsc".equals(align))) {

            Page<ForumPost> forumPosts = service.getForumList(page, topic, align);
    
            Page<ForumPostDTO> forumPostDTOs = forumPosts.map(post -> new ForumPostDTO(post));
            
            resp.put("forumPosts", forumPostDTOs);
        }else{
            resp.put("error", "잘못된 요청입니다.");
        }
        
        return resp;
    }

    // 포럼 게시글 상세보기, 해당하는 댓글 불러오기
    // 포럼 게시글 검색하기
}

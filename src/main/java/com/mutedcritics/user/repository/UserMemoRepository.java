package com.mutedcritics.user.repository;

import com.mutedcritics.entity.UserMemo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserMemoRepository extends JpaRepository<UserMemo, Integer> {
    List<UserMemo> findByUserUserIdOrderByCreatedAtDesc(String userId);

    boolean existsByUserUserIdAndMemberMemberId(String userUserId, String memberMemberId);
}

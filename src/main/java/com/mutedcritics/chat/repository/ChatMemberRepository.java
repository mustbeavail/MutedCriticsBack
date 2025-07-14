package com.mutedcritics.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mutedcritics.entity.ChatMember;
import com.mutedcritics.entity.ChatMemberId;

@Repository
public interface ChatMemberRepository extends JpaRepository<ChatMember, ChatMemberId>, ChatMemberRepositoryCustom {

}

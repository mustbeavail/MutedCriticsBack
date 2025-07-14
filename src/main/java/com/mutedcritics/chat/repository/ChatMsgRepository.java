package com.mutedcritics.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mutedcritics.entity.ChatMsg;

@Repository
public interface ChatMsgRepository extends JpaRepository<ChatMsg, Integer>, ChatMsgRepositoryCustom {

}

package com.mutedcritics.chat.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.mutedcritics.entity.ChatMsg;

public interface ChatMsgRepositoryCustom {
    List<ChatMsg> findChatMessages(int roomIdx, Pageable pageable);

    Optional<ChatMsg> findLastMessage(int roomIdx);
}

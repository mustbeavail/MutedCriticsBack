package com.mutedcritics.chat.repository;

import java.util.List;
import java.util.Optional;

import com.mutedcritics.entity.ChatMsg;

public interface ChatMsgRepositoryCustom {
    List<ChatMsg> findChatMessages(int roomIdx);

    Optional<ChatMsg> findLastMessage(int roomIdx);
}

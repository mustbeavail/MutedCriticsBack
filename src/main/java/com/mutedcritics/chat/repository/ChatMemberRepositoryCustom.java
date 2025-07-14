package com.mutedcritics.chat.repository;

import java.util.List;

import com.mutedcritics.entity.ChatMember;

public interface ChatMemberRepositoryCustom {
    List<ChatMember> findRoomParticipants(int roomIdx);

    boolean hasTargetMember(int roomIdx, String targetMemberId);

    void leaveChatRoom(int roomIdx, String memberId);
}

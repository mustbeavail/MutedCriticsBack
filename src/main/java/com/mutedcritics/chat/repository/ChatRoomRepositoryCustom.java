package com.mutedcritics.chat.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mutedcritics.entity.ChatRoom;

public interface ChatRoomRepositoryCustom {

    Optional<ChatRoom> findExistingPrivateRoom(String memberId, String targetMemberId);

    Page<ChatRoom> findMyChatRooms(String memberId, String searchKeyword, Pageable pageable);

    void updateRoomName(int roomIdx, String newRoomName);

    long countActiveMembers(int roomIdx);

}

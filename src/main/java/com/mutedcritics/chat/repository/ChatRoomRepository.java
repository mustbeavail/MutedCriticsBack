package com.mutedcritics.chat.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mutedcritics.entity.ChatRoom;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Integer>, ChatRoomRepositoryCustom {

    @Query("SELECT c.memberId FROM ChatRoom c WHERE c.roomIdx = :roomIdx AND c.memberId != :memberId")
    Optional<String> findChatMemberByRoomIdx(int roomIdx, String memberId);
}

package com.mutedcritics.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class ChatRoomDTO {

    private int roomIdx;
    private String roomName;
    private String ownerId;
    private String ownerName;
    private String roomType; // 'private' or 'group'
    private LocalDateTime createdAt;
    private List<ChatMemberDTO> participants;
    private int participantCount;
    private ChatMessageDTO lastMessage; // 마지막 메시지
}

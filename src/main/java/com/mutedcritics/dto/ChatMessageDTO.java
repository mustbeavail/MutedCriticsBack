package com.mutedcritics.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ChatMessageDTO {

    private int msgIdx;
    private int roomIdx;
    private String senderId;
    private String senderName;
    private String msgContent;
    private LocalDateTime sentAt;
    private MessageType type;

    public enum MessageType {
        CHAT, // 일반 채팅 메시지
        JOIN, // 채팅방 입장
        LEAVE // 채팅방 퇴장
    }

}

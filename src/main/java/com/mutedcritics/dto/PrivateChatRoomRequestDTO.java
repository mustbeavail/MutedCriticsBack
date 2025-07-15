package com.mutedcritics.dto;

import lombok.Data;

@Data
public class PrivateChatRoomRequestDTO {
    private String memberId;
    private String targetMemberId;
}

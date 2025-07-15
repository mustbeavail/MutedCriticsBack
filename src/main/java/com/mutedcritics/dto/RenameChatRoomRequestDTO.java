package com.mutedcritics.dto;

import lombok.Data;

@Data
public class RenameChatRoomRequestDTO {
    private String newRoomName;
    private String memberId;
}

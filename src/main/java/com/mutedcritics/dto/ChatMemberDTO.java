package com.mutedcritics.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ChatMemberDTO {

    private String memberId;
    private String memberName;
    private String email;
    private String companyPhone;
    private String personalPhone;
    private String department;
    private String position;
    private LocalDateTime joinAt;
    private boolean activeYn;

}

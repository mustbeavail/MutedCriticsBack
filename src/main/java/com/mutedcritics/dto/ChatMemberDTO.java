package com.mutedcritics.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ChatMemberDTO {

    private String memberId;
    private String memberName;
    private String email;
    private String companyPhone;
    private String personalPhone;
    private String department;
    private String position;
    private LocalDate joinAt;
    private LocalDate withdrawalAt;
    private boolean activeYn;

}

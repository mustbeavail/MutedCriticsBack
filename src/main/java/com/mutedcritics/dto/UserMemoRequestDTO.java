package com.mutedcritics.dto;

import lombok.Data;

@Data
public class UserMemoRequestDTO {
    private String memberId;
    private String userId;
    private String memo;
}

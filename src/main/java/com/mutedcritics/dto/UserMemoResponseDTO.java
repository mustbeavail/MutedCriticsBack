package com.mutedcritics.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserMemoResponseDTO {
    private int memoIdx;
    private String memberId;
    private String userId;
    private String memoContent;
    private String createdAt;
    private String updatedAt;
}

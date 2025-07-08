package com.mutedcritics.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO {

    private int responseIdx;
    private int inquiryIdx;
    private String userId;
    private String content;
    private String resType;
    private String agentId;
    private LocalDateTime createdAt;

}

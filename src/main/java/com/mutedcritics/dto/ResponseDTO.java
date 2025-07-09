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
    private String agentId;
    private String content;
    private LocalDateTime createdAt;

}

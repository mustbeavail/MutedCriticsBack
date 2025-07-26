package com.mutedcritics.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class InquiryDTO {

    private int inquiryIdx;
    private String userId;
    private String reportedUserId;
    private String type;
    private String category;
    private String title;
    private String content;
    private String status;
    private LocalDateTime createdAt;

}
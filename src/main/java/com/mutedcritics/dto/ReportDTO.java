package com.mutedcritics.dto;

import java.time.LocalDateTime;

import com.mutedcritics.entity.Inquiry;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {

    private int inquiryIdx;
    private String userId;
    private String reportedUserId;
    private String type;
    private String category;
    private String title;
    private String content;
    private String status;
    private LocalDateTime createdAt;

    public ReportDTO(Inquiry inquiry) {
        this.inquiryIdx = inquiry.getInquiryIdx();
        this.type = inquiry.getType();
        this.category = inquiry.getCategory();
        this.title = inquiry.getTitle();
        this.content = inquiry.getContent();
        this.status = inquiry.getStatus();
        this.createdAt = inquiry.getCreatedAt();

        if (inquiry.getUser() != null) {
            this.userId = inquiry.getUser().getUserId();
        }

        if (inquiry.getReportedUser() != null) {
            this.reportedUserId = inquiry.getReportedUser().getUserId();
        }
    }

}

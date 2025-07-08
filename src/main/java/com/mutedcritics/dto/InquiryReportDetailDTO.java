package com.mutedcritics.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InquiryReportDetailDTO {

    private int inquiryIdx;
    private String userId;
    private String reportedUserId;
    private String type;
    private String category;
    private String title;
    private String content;
    private String status;

    private List<ResponseDTO> responses;

}

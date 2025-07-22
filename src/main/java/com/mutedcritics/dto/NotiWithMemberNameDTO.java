package com.mutedcritics.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class NotiWithMemberNameDTO {
    
    private int notiIdx;
    private String contentPre;
    private int relatedIdx;
    private boolean readYn;
    private String notiType;
    private LocalDateTime createdAt;
    private String memberName;  // receiver의 memberName만 추가
    
    public NotiWithMemberNameDTO(int notiIdx, String contentPre, int relatedIdx, 
                                boolean readYn, String notiType, LocalDateTime createdAt, 
                                String memberName) {
        this.notiIdx = notiIdx;
        this.contentPre = contentPre;
        this.relatedIdx = relatedIdx;
        this.readYn = readYn;
        this.notiType = notiType;
        this.createdAt = createdAt;
        this.memberName = memberName;
    }
} 
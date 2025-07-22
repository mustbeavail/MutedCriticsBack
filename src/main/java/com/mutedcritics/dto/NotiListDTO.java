package com.mutedcritics.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotiListDTO {

    private int notiIdx;
    private String contentPre;
    private int relatedIdx;
    private boolean readYn;
    private String notiType;
    private LocalDateTime createdAt;
    private String memberId;
    private String receiverId;
    private String memberName;
}

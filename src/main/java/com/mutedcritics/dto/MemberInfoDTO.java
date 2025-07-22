package com.mutedcritics.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberInfoDTO {
    private String memberName;
    private String memberId;
    private String deptName;
    private String positionName;
    private String email;
    private String officePhone;
    private String mobilePhone;
}

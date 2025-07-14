package com.mutedcritics.dto;

import lombok.Data;

@Data
public class MemberSerachDTO {

    private String memberId;
    private String memberName;
    private String email;
    private String companyPhone;
    private String personalPhone;
    private String department;
    private String position;

}

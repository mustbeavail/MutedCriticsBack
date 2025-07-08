package com.mutedcritics.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class UserDTO {

    private String user_id;
    private String user_pw;
    private String user_nick;
    private String user_gender;
    private String phone;
    private String region;
    private LocalDate join_date;
    private LocalDate dormant_date;
    private LocalDate withdraw_date;
    private String user_type;
    private boolean vip_yn;
    private boolean receive_yn;
    private LocalDate receive_yn_at;

}

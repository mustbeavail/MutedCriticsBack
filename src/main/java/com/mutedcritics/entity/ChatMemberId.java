package com.mutedcritics.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Embeddable
@Data
@EqualsAndHashCode
public class ChatMemberId implements Serializable {

    @Column(name = "room_idx")
    private int roomIdx;

    @Column(name = "member_id", length = 50)
    private String memberId;

}
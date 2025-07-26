package com.mutedcritics.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;

@Entity
@Data
@Table(name = "chat_member")
public class ChatMember {

    @EmbeddedId
    private ChatMemberId id;

    // 복합키
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roomIdx")
    @JoinColumn(name = "room_idx")
    private ChatRoom chatRoom;

    // 복합키
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("memberId")
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "join_at")
    @CreationTimestamp
    private LocalDate joinAt;

    @Column(name = "left_at")
    private LocalDate leftAt;

    @Column(name = "active_yn")
    private boolean activeYn;

}

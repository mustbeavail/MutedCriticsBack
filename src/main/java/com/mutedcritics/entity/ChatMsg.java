package com.mutedcritics.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;

@Entity
@Data
@Table(name = "chat_msg")
public class ChatMsg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "msg_idx")
    private int msgIdx;

    // 외래키(chat_room 테이블의 room_idx)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_idx")
    private ChatRoom chatRoom;

    // 외래키(member 테이블의 member_id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private Member sender;

    @Lob
    @Column(name = "msg_content", columnDefinition = "longtext")
    private String msgContent;

    @Column(name = "sent_at")
    @CreationTimestamp
    private LocalDateTime sentAt;

}

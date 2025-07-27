package com.mutedcritics.chat.repository.impl;

import java.util.List;
import java.util.Optional;

import com.mutedcritics.chat.repository.ChatMsgRepositoryCustom;
import com.mutedcritics.entity.ChatMsg;
import com.mutedcritics.entity.QChatMsg;
import com.mutedcritics.entity.QMember;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChatMsgRepositoryImpl implements ChatMsgRepositoryCustom {

    private final JPAQueryFactory factory;
    QChatMsg chatMsg = QChatMsg.chatMsg;
    QMember member = QMember.member;

    // 채팅 메시지 조회
    @Override
    public List<ChatMsg> findChatMessages(int roomIdx) {
        return factory.selectFrom(chatMsg)
                .join(chatMsg.sender, member).fetchJoin()
                .where(chatMsg.chatRoom.roomIdx.eq(roomIdx))
                .orderBy(chatMsg.sentAt.asc())
                .fetch();
    }

    // 마지막 메시지 조회
    @Override
    public Optional<ChatMsg> findLastMessage(int roomIdx) {
        ChatMsg lastMsg = factory.selectFrom(chatMsg)
                .join(chatMsg.sender, member).fetchJoin()
                .where(chatMsg.chatRoom.roomIdx.eq(roomIdx))
                .orderBy(chatMsg.sentAt.desc())
                .limit(1)
                .fetchOne();

        return Optional.ofNullable(lastMsg);
    }

}

package com.mutedcritics.chat.repository.impl;

import java.time.LocalDateTime;
import java.util.List;

import com.mutedcritics.chat.repository.ChatMemberRepositoryCustom;
import com.mutedcritics.entity.ChatMember;
import com.mutedcritics.entity.QChatMember;
import com.mutedcritics.entity.QMember;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChatMemberRepositoryImpl implements ChatMemberRepositoryCustom {

    private final JPAQueryFactory factory;
    QChatMember chatMember = QChatMember.chatMember;
    QMember member = QMember.member;

    // 채팅방 참여자 조회
    @Override
    public List<ChatMember> findRoomParticipants(int roomIdx) {
        return factory.selectFrom(chatMember)
                .join(chatMember.member, member).fetchJoin()
                .where(chatMember.chatRoom.roomIdx.eq(roomIdx).and(chatMember.activeYn.eq(true)))
                .fetch();
    }

    @Override
    public boolean hasTargetMember(int roomIdx, String targetMemberId) {
        long count = factory.select(chatMember.count())
                .from(chatMember)
                .where(chatMember.chatRoom.roomIdx.eq(roomIdx).and(chatMember.member.memberId.eq(targetMemberId))
                        .and(chatMember.activeYn.eq(true)))
                .fetchOne();

        return count > 0;
    }

    // 채팅방 나가기
    @Override
    public void leaveChatRoom(int roomIdx, String memberId) {
        factory.update(chatMember)
                .set(chatMember.activeYn, false)
                .set(chatMember.leftAt, LocalDateTime.now())
                .where(chatMember.chatRoom.roomIdx.eq(roomIdx).and(chatMember.member.memberId.eq(memberId)))
                .execute();
    }

}

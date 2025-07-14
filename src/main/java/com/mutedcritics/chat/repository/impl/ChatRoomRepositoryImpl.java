package com.mutedcritics.chat.repository.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.mutedcritics.chat.repository.ChatRoomRepositoryCustom;
import com.mutedcritics.entity.ChatRoom;
import com.mutedcritics.entity.QChatMember;
import com.mutedcritics.entity.QChatRoom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements ChatRoomRepositoryCustom {

    private final JPAQueryFactory factory;
    QChatRoom chatRoom = QChatRoom.chatRoom;
    QChatMember chatMember = QChatMember.chatMember;

    @Override
    public Optional<ChatRoom> findExistingPrivateRoom(String memberId, String targetMemberId) {
        // 내가 참여한 private 채팅방 중에서 대상자도 참여한 방 찾기
        List<ChatRoom> myPrivateRooms = factory
                .select(chatRoom)
                .from(chatRoom)
                .join(chatMember).on(chatMember.chatRoom.roomIdx.eq(chatRoom.roomIdx))
                .where(chatRoom.roomType.eq("private")
                        .and(chatMember.member.memberId.eq(memberId))
                        .and(chatMember.activeYn.eq(true)))
                .fetch();

        // 각 방에 대해 대상자가 참여하고 있는지 확인
        for (ChatRoom room : myPrivateRooms) {
            long targetCount = factory
                    .select(chatMember.count())
                    .from(chatMember)
                    .where(chatMember.chatRoom.roomIdx.eq(room.getRoomIdx())
                            .and(chatMember.member.memberId.eq(targetMemberId))
                            .and(chatMember.activeYn.eq(true)))
                    .fetchOne();

            if (targetCount > 0) {
                return Optional.of(room);
            }
        }

        return Optional.empty();
    }

    @Override
    public Page<ChatRoom> findMyChatRooms(String memberId, String searchKeyword, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        // 내가 참여중인 채팅방
        builder.and(chatMember.member.memberId.eq(memberId));
        builder.and(chatMember.activeYn.eq(true));

        // 검색 조건 (채팅방 이름으로 검색)
        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            builder.and(chatRoom.roomName.containsIgnoreCase(searchKeyword));
        }

        // 총 개수 조회
        long total = factory.select(chatRoom.countDistinct())
                .from(chatRoom)
                .join(chatMember).on(chatMember.chatRoom.roomIdx.eq(chatRoom.roomIdx))
                .where(builder)
                .fetchOne();

        // 채팅방 목록 조회
        List<ChatRoom> rooms = factory.select(chatRoom)
                .distinct()
                .from(chatRoom)
                .join(chatMember).on(chatMember.chatRoom.roomIdx.eq(chatRoom.roomIdx))
                .where(builder)
                .orderBy(chatRoom.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return new PageImpl<>(rooms, pageable, total);
    }

    // 채팅방 이름 변경
    @Override
    public void updateRoomName(int roomIdx, String newRoomName) {
        factory.update(chatRoom)
                .set(chatRoom.roomName, newRoomName)
                .where(chatRoom.roomIdx.eq(roomIdx))
                .execute();
    }

    // 채팅방 활성 멤버 수 조회
    @Override
    public long countActiveMembers(int roomIdx) {
        long count = factory.select(chatMember.count())
                .from(chatMember)
                .where(chatMember.chatRoom.roomIdx.eq(roomIdx).and(chatMember.activeYn.eq(true)))
                .fetchOne();
        return count;
    }

}

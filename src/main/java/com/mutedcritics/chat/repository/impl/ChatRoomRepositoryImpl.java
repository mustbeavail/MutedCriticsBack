package com.mutedcritics.chat.repository.impl;

import java.util.List;
import java.util.Optional;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.JPAExpressions;
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
    public Page<ChatRoom> findMyChatRooms(String memberId, String searchType, String searchKeyword, String sortBy, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        // 내가 참여중인 채팅방
        builder.and(chatMember.member.memberId.eq(memberId));
        builder.and(chatMember.activeYn.eq(true));

        // 채팅방 이름 검색
        if ("roomName".equals(searchType) && searchKeyword != null && !searchKeyword.isEmpty()) {
            builder.and(chatRoom.roomName.containsIgnoreCase(searchKeyword));
        }

        // 멤버 이름 검색
        if ("memberName".equals(searchType) && searchKeyword != null && !searchKeyword.isEmpty()) {
            QChatMember cm2 = new QChatMember("cm2");
            builder.and(JPAExpressions
                    .selectOne()
                    .from(cm2)
                    .where(
                            cm2.chatRoom.roomIdx.eq(chatRoom.roomIdx),
                            cm2.member.memberName.containsIgnoreCase(searchKeyword),
                            cm2.activeYn.eq(true)
                    )
                    .exists()
            );
        }

        // 총 개수 조회
        long total = factory.select(chatRoom.countDistinct())
                .from(chatRoom)
                .join(chatMember).on(chatMember.chatRoom.roomIdx.eq(chatRoom.roomIdx))
                .where(builder)
                .fetchOne();


        // 정렬 기준 동적 결정
        OrderSpecifier<?> orderSpecifier;
        if ("dateASC".equalsIgnoreCase(sortBy)) {
            orderSpecifier = chatRoom.createdAt.asc(); // 생성일 오름차순
        } else {
            orderSpecifier = chatRoom.createdAt.desc(); // 기본 : 생성일 내림차순 (dateDESC)
        }

        // 채팅방 목록 조회
        List<ChatRoom> rooms = factory.select(chatRoom)
                .distinct()
                .from(chatRoom)
                .join(chatMember).on(chatMember.chatRoom.roomIdx.eq(chatRoom.roomIdx))
                .where(builder)
                .orderBy(orderSpecifier)
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

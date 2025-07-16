package com.mutedcritics.notice.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mutedcritics.chat.repository.ChatRoomRepository;
import com.mutedcritics.entity.ChatMsg;
import com.mutedcritics.entity.Noti;
import com.mutedcritics.notice.repository.NoticeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepo;
    private final ChatRoomRepository chatRoomRepo;

    // 메시지 알림 저장
    public void saveChatNotification(ChatMsg chatMessage) {

        String contentPre = chatMessage.getMsgContent();
        String memberId = chatMessage.getSender().getMemberId();
        LocalDateTime createdAt = chatMessage.getSentAt();

        int roomIdx = chatMessage.getChatRoom().getRoomIdx();
        String receiverId = chatRoomRepo.findChatMemberByRoomIdx(roomIdx, memberId)
        .orElseThrow(() -> new RuntimeException("수신자를 찾을 수 없습니다."));
        int relatedIdx = roomIdx;
        boolean readYn = false;
        String notiType = "chat";

        Noti noti = new Noti();
        noti.setContentPre(contentPre);
        noti.getMember().setMemberId(memberId);
        noti.getReceiver().setMemberId(receiverId);
        noti.setCreatedAt(createdAt);
        noti.setRelatedIdx(relatedIdx);
        noti.setReadYn(readYn);
        noti.setNotiType(notiType);

        noticeRepo.save(noti);
    }

    // 알림 읽음 처리
    public boolean readNotice(int notiIdx) {
        Noti noti = noticeRepo.findById(notiIdx)
                .orElseThrow(() -> new RuntimeException("알림을 찾을 수 없습니다."));

        noti.setReadYn(true);
        boolean success = noticeRepo.save(noti) != null;

        return success;
    }

    // 채팅 알림 목록 조회
    public List<Noti> getChatNoticeList(String memberId) {

        List<Noti> notiList = noticeRepo.findAllByReceiverId(memberId);

        return notiList;
    }

    // 통계 알림 목록 조회
    public List<Noti> getStatNoticeList() {

        List<Noti> notiList = noticeRepo.findAllByNotiType();

        return notiList;
    }



}

package com.mutedcritics.notice.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.mutedcritics.chat.repository.ChatRoomRepository;
import com.mutedcritics.entity.ChatMsg;
import com.mutedcritics.entity.Member;
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
        Member member = new Member();
        member.setMemberId(memberId);
        noti.setMember(member);
        Member receiver = new Member();
        receiver.setMemberId(receiverId);
        noti.setReceiver(receiver);
        noti.setContentPre(contentPre);
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
    public List<Map<String, Object>> getChatNoticeList(String memberId) {

        List<Noti> notiList = noticeRepo.findAllByReceiverId(memberId);
        List<Map<String, Object>> resp = new ArrayList<>();

        for (Noti noti : notiList) {
            int notiIdx = noti.getNotiIdx();
            String senderId = noti.getMember().getMemberId();
            String receiverId = noti.getReceiver().getMemberId();
            String contentPre = noti.getContentPre();
            LocalDateTime createdAt = noti.getCreatedAt();
            String notiType = noti.getNotiType();
            int relatedIdx = noti.getRelatedIdx();
            boolean readYn = noti.isReadYn();

            Map<String, Object> notiMap = new HashMap<>();
            notiMap.put("notiIdx", notiIdx);
            notiMap.put("senderId", senderId);
            notiMap.put("receiverId", receiverId);
            notiMap.put("contentPre", contentPre);
            notiMap.put("createdAt", createdAt);
            notiMap.put("notiType", notiType);
            notiMap.put("relatedIdx", relatedIdx);
            notiMap.put("readYn", readYn);
            resp.add(notiMap);
        }

        return resp;
    }

    // 통계 알림 목록 조회
    public List<Map<String, Object>> getStatNoticeList(LocalDateTime firstDayOfMonth, LocalDateTime lastDayOfMonth) {

        List<Noti> notiList = noticeRepo.findAllByNotiType(firstDayOfMonth, lastDayOfMonth);
        List<Map<String, Object>> resp = new ArrayList<>();

        for (Noti noti : notiList) {
            int notiIdx = noti.getNotiIdx();
            String memberId = noti.getMember().getMemberId();
            String receiverId = noti.getReceiver().getMemberId();
            String contentPre = noti.getContentPre();
            LocalDateTime createdAt = noti.getCreatedAt();
            String notiType = noti.getNotiType();
            int relatedIdx = noti.getRelatedIdx();
            boolean readYn = noti.isReadYn();

            Map<String, Object> notiMap = new HashMap<>();
            notiMap.put("notiIdx", notiIdx);
            notiMap.put("memberId", memberId);
            notiMap.put("receiverId", receiverId);
            notiMap.put("contentPre", contentPre);
            notiMap.put("createdAt", createdAt);
            notiMap.put("notiType", notiType);
            notiMap.put("relatedIdx", relatedIdx);
            notiMap.put("readYn", readYn);
            resp.add(notiMap);
        }

        return resp;
    }

    // 매출 감소 알림 저장
    public boolean saveRevenueDecreaseNotice(
        long totalRevenue, long totalRevenue2, long totalRevenue3,
        YearMonth YM, YearMonth YM2, YearMonth YM3) {

        DateTimeFormatter YM_FMT = DateTimeFormatter.ofPattern("yyyy-MM");
        String ym1Str = YM.format(YM_FMT);   // 전달
        String ym2Str = YM2.format(YM_FMT);   // 전전달
        String ym3Str = YM3.format(YM_FMT);  // 전전전달

        String memberId = "admin";
        String receiverId = "admin";
        String contentPre = String.format(
            "3개월 연속 매출 하락: %s %,d원 ↓ %s %,d원 ↓ %s %,d원.",
            ym3Str, totalRevenue3,
            ym2Str, totalRevenue2,
            ym1Str, totalRevenue
        );
        int relatedIdx = 0;
        boolean readYn = false;
        LocalDateTime createdAt = LocalDateTime.now();
        String notiType = "revenueDecreaseStat";

        Noti noti = new Noti();
        Member member = new Member();
        member.setMemberId(memberId);
        noti.setMember(member);
        Member receiver = new Member();
        receiver.setMemberId(receiverId);
        noti.setReceiver(receiver);
        noti.setContentPre(contentPre);
        noti.setRelatedIdx(relatedIdx);
        noti.setReadYn(readYn);
        noti.setCreatedAt(createdAt);
        noti.setNotiType(notiType);

        boolean success = noticeRepo.save(noti) != null;

        return success;
    }

    // 아이템 매출 편중 심화 알림 저장
    public boolean saveConcentratedItemNotice(List<Map<String, Object>> concentratedItems) {

        boolean success = false;
        int successCount = 0;

        for (Map<String, Object> item : concentratedItems) {
            String itemName = (String) item.get("itemName");
            BigDecimal howManyTimes = (BigDecimal) item.get("howManyTimes");
            BigDecimal itemRevenueRate = (BigDecimal) item.get("itemRevenueRate");
            String memberId = "admin";
            String receiverId = "admin";
            long totalPct = itemRevenueRate.multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.HALF_UP).longValue();
            long avgPct = howManyTimes.multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.HALF_UP).longValue();
            String contentPre = String.format(
                "아이템 매출 편중 심화: %s 전체 매출의 %,d%% 평균매출액의 %,d%%",
                itemName, totalPct, avgPct
            );
            int relatedIdx = (int) item.get("itemIdx");
            boolean readYn = false;
            LocalDateTime createdAt = LocalDateTime.now();
            String notiType = "concentratedItemStat";

            Noti noti = new Noti();
            Member member = new Member();
            member.setMemberId(memberId);
            noti.setMember(member);
            Member receiver = new Member();
            receiver.setMemberId(receiverId);
            noti.setReceiver(receiver); 
            noti.setContentPre(contentPre);
            noti.setRelatedIdx(relatedIdx);
            noti.setReadYn(readYn);
            noti.setCreatedAt(createdAt);
            noti.setNotiType(notiType);

            success = noticeRepo.save(noti) != null;

            if (success){
                successCount++;
            }else{
                log.error("아이템 매출 편중 심화 알림 전송 실패: {}", itemName);
            }
        }
        return successCount == concentratedItems.size();
    }
}

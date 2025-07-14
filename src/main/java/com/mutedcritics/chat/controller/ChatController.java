package com.mutedcritics.chat.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mutedcritics.chat.service.ChatService;
import com.mutedcritics.dto.ChatMessageDTO;
import com.mutedcritics.dto.ChatRoomDTO;
import com.mutedcritics.dto.MemberSerachDTO;
import com.mutedcritics.entity.ChatMsg;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin
@RestController
@Slf4j
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    // 웹소켓 메시지 처리
    @MessageMapping("/chat/{roomIdx}")
    public void sendMessage(@DestinationVariable int roomIdx,
            @Payload ChatMessageDTO chatMessage) {

        log.info("메시지 수신 - 방번호: {}, 발신자: {}, 내용: {}",
                roomIdx, chatMessage.getSenderId(), chatMessage.getMsgContent());

        ChatMsg savedMessage = chatService.saveMessage(chatMessage);
        ChatMessageDTO messageToSend = chatService.convertToDTO(savedMessage);

        if (messageToSend == null) {
            log.error("메시지 DTO 변환 실패: savedMessage={}", savedMessage);
            return;
        }

        String destination = "/topic/chat/" + roomIdx;
        log.info("메시지 전송 - 목적지: {}, 내용: {}", destination, messageToSend);
        messagingTemplate.convertAndSend(destination, messageToSend);
    }

    // 1대1 채팅방 생성 또는 기존 방 조회
    @PostMapping("/room/private")
    public ResponseEntity<ChatRoomDTO> createOrGetPrivateRoom(
            @RequestParam String memberId,
            @RequestParam String targetMemberId) {
        ChatRoomDTO chatRoom = chatService.createOrGetPrivateRoom(memberId, targetMemberId);
        return ResponseEntity.ok(chatRoom);
    }

    // 내 채팅방 목록 조회(페이징)
    @GetMapping("/rooms")
    public ResponseEntity<Page<ChatRoomDTO>> getMyChatRooms(
            @RequestParam String memberId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String searchKeyword) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ChatRoomDTO> chatRooms = chatService.getMyChatRooms(memberId, searchKeyword, pageable);
        return ResponseEntity.ok(chatRooms);
    }

    // 채팅방 메시지 조회
    @GetMapping("/room/{roomIdx}/messages")
    public ResponseEntity<List<ChatMessageDTO>> getChatMessages(
            @PathVariable int roomIdx,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<ChatMessageDTO> messages = chatService.getChatMessages(roomIdx, pageable);
        return ResponseEntity.ok(messages);
    }

    // 채팅 대상 검색 (직원 검색)
    @GetMapping("/members/search")
    public ResponseEntity<List<MemberSerachDTO>> searchMembers(
            @RequestParam String keyword,
            @RequestParam String currentMemberId) {
        List<MemberSerachDTO> members = chatService.searchMembers(keyword, currentMemberId);
        return ResponseEntity.ok(members);
    }

    // 채팅방 나가기
    @PostMapping("/room/{roomIdx}/leave")
    public ResponseEntity<String> leaveChatRoom(
            @PathVariable int roomIdx,
            @RequestParam String memberId) {
        chatService.leaveChatRoom(roomIdx, memberId);
        return ResponseEntity.ok("채팅방에서 나갔습니다.");
    }

    // 채팅방 이름 변경(방장만 가능)
    @PostMapping("/room/{roomIdx}/rename")
    public ResponseEntity<String> renameChatRoom(@PathVariable int roomIdx,
            @RequestBody String newRoomName,
            @RequestParam String memberid) {
        chatService.renameChatRoom(roomIdx, newRoomName, memberid);
        return ResponseEntity.ok("채팅방 이름이 변경되었습니다.");
    }

}

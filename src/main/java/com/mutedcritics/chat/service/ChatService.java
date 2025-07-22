package com.mutedcritics.chat.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mutedcritics.chat.repository.ChatMemberRepository;
import com.mutedcritics.chat.repository.ChatMsgRepository;
import com.mutedcritics.chat.repository.ChatRoomRepository;
import com.mutedcritics.dto.ChatMemberDTO;
import com.mutedcritics.dto.ChatMessageDTO;
import com.mutedcritics.dto.ChatRoomDTO;
import com.mutedcritics.dto.LeaveChatRoomRequestDTO;
import com.mutedcritics.dto.MemberSerachDTO;
import com.mutedcritics.dto.PrivateChatRoomRequestDTO;
import com.mutedcritics.dto.RenameChatRoomRequestDTO;
import com.mutedcritics.entity.ChatMember;
import com.mutedcritics.entity.ChatMemberId;
import com.mutedcritics.entity.ChatMsg;
import com.mutedcritics.entity.ChatRoom;
import com.mutedcritics.entity.Member;
import com.mutedcritics.member.repository.MemberRepository;
import com.mutedcritics.notice.service.NoticeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMsgRepository chatMsgRepository;
    private final ChatMemberRepository chatMemberRepository;
    private final MemberRepository memberRepository;
    private final NoticeService notiService;

    // 메시지 저장
    @Transactional
    public ChatMsg saveMessage(ChatMessageDTO messageDTO) {
        ChatRoom room = chatRoomRepository.findById(messageDTO.getRoomIdx())
                .orElseThrow(() -> new RuntimeException("채팅방을 찾을 수 없습니다."));

        Member sender = memberRepository.findById(messageDTO.getSenderId())
                .orElseThrow(() -> new RuntimeException("발신자를 찾을 수 없습니다."));

        ChatMsg chatMessage = new ChatMsg();
        chatMessage.setChatRoom(room);
        chatMessage.setSender(sender);
        chatMessage.setMsgContent(messageDTO.getMsgContent());
        chatMessage.setSentAt(LocalDateTime.now());

        ChatMsg savedMessage = chatMsgRepository.save(chatMessage);


        if (savedMessage != null || savedMessage.getMsgIdx() > 0) {
            // 메시지 알림 저장하기
            notiService.saveChatNotification(chatMessage);
            return savedMessage;
        } else {
            throw new RuntimeException("메시지 저장 실패");
        }

    }

    // 1대1 채팅방 생성 또는 기존 방 조회
    @Transactional
    public ChatRoomDTO createOrGetPrivateRoom(PrivateChatRoomRequestDTO request) {
        // Repository에서 기존 방 검색
        Optional<ChatRoom> existingRoom = chatRoomRepository.findExistingPrivateRoom(request.getMemberId(), request.getTargetMemberId());

        if (existingRoom.isPresent()) {
            return convertToRoomDTO(existingRoom.get());
        }

        // 새로운 1대1 채팅방 생성
        Member owner = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new RuntimeException("방장을 찾을 수 없습니다."));
        Member targetMember = memberRepository.findById(request.getTargetMemberId())
                .orElseThrow(() -> new RuntimeException("대상 회원을 찾을 수 없습니다."));

        ChatRoom newRoom = new ChatRoom();
        newRoom.setRoomType("private");
        newRoom.setOwner(owner);
        newRoom.setCreatedAt(LocalDateTime.now());
        newRoom.setRoomName(targetMember.getMemberName());

        ChatRoom savedRoom = chatRoomRepository.save(newRoom);

        // 채팅방 멤버 추가
        addMemberToRoom(savedRoom.getRoomIdx(), request.getMemberId());
        addMemberToRoom(savedRoom.getRoomIdx(), request.getTargetMemberId());

        return convertToRoomDTO(savedRoom);
    }

    // 내 채팅방 목록 조회
    @Transactional(readOnly = true)
    public Page<ChatRoomDTO> getMyChatRooms(String memberId, String searchType, String searchKeyword, Pageable pageable) {
        Page<ChatRoom> roomPage = chatRoomRepository.findMyChatRooms(memberId, searchType, searchKeyword, pageable);

        return roomPage.map(this::convertToRoomDTO);
    }

    // 채팅방 메시지 불러오기
    @Transactional(readOnly = true)
    public List<ChatMessageDTO> getChatMessages(int roomIdx, Pageable pageable) {
        List<ChatMsg> messages = chatMsgRepository.findChatMessages(roomIdx, pageable);

        return messages.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 직원 검색
    @Transactional(readOnly = true)
    public List<MemberSerachDTO> searchMembers(String keyword, String currentMemberId) {
        List<Member> members = memberRepository.searchMembers(keyword, currentMemberId);

        return members.stream()
                .map(this::convertToMemberSearchDTO)
                .collect(Collectors.toList());
    }

    // 채팅방 나가기
    @Transactional
    public void leaveChatRoom(int roomIdx, LeaveChatRoomRequestDTO request) {
        // Repository에서 나가기 처리
        chatMemberRepository.leaveChatRoom(roomIdx, request.getMemberId());

        // 채팅방 멤버 수 확인 후 채팅방 삭제 결정
        long activeMemberCount = chatRoomRepository.countActiveMembers(roomIdx);

        if (activeMemberCount == 0) {
            chatRoomRepository.deleteById(roomIdx); // 엔티티에서 cascade 설정해야할 듯(완료!)
        }
    }

    // 채팅방 이름 변경(방장만 가능)
    @Transactional
    public void renameChatRoom(int roomIdx, RenameChatRoomRequestDTO request) {
        // 방장 확인
        ChatRoom room = chatRoomRepository.findById(roomIdx)
                .orElseThrow(() -> new RuntimeException("채팅방을 찾을 수 없습니다."));

        if (!room.getOwner().getMemberId().equals(request.getMemberId())) {
            throw new RuntimeException("방장만 채팅방 이름을 변경할 수 있습니다.");
        }

        // 채팅방 이름 변경
        chatRoomRepository.updateRoomName(roomIdx, request.getNewRoomName());
    }

    // 채팅방에 멤버 추가
    private void addMemberToRoom(int roomIdx, String memberId) {
        ChatRoom room = chatRoomRepository.findById(roomIdx)
                .orElseThrow(() -> new RuntimeException("채팅방을 찾을 수 없습니다."));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));

        ChatMemberId chatMemberId = new ChatMemberId();
        chatMemberId.setRoomIdx(roomIdx);
        chatMemberId.setMemberId(memberId);

        ChatMember chatMember = new ChatMember();
        chatMember.setId(chatMemberId);
        chatMember.setChatRoom(room);
        chatMember.setMember(member);
        chatMember.setActiveYn(true);
        chatMember.setJoinAt(LocalDateTime.now());

        chatMemberRepository.save(chatMember);
    }

    // DTO 변환 메서드
    public ChatMessageDTO convertToDTO(ChatMsg chatMsg) {
        ChatMessageDTO dto = new ChatMessageDTO();
        dto.setMsgIdx(chatMsg.getMsgIdx());
        dto.setRoomIdx(chatMsg.getChatRoom().getRoomIdx());
        dto.setSenderId(chatMsg.getSender().getMemberId());
        dto.setSenderName(chatMsg.getSender().getMemberName());
        dto.setMsgContent(chatMsg.getMsgContent());
        dto.setSentAt(chatMsg.getSentAt());
        dto.setType(ChatMessageDTO.MessageType.CHAT);
        return dto;
    }

    private ChatRoomDTO convertToRoomDTO(ChatRoom chatRoom) {
        ChatRoomDTO dto = new ChatRoomDTO();
        dto.setRoomIdx(chatRoom.getRoomIdx());
        dto.setRoomName(chatRoom.getRoomName());
        dto.setOwnerId(chatRoom.getOwner().getMemberId());
        dto.setOwnerName(chatRoom.getOwner().getMemberName());
        dto.setRoomType(chatRoom.getRoomType());
        dto.setCreatedAt(chatRoom.getCreatedAt());

        // 참여자 정보 추가
        List<ChatMemberDTO> participants = getRoomParticipants(chatRoom.getRoomIdx());
        dto.setParticipants(participants);
        dto.setParticipantCount(participants.size());

        // 마지막 메시지 추가
        chatMsgRepository.findLastMessage(chatRoom.getRoomIdx())
                .ifPresent(msg -> dto.setLastMessage(convertToDTO(msg)));

        return dto;
    }

    private List<ChatMemberDTO> getRoomParticipants(int roomIdx) {
        List<ChatMember> members = chatMemberRepository.findRoomParticipants(roomIdx);

        return members.stream()
                .map(this::convertToMemberDTO)
                .collect(Collectors.toList());
    }

    private ChatMemberDTO convertToMemberDTO(ChatMember chatMember) {
        ChatMemberDTO dto = new ChatMemberDTO();
        dto.setMemberId(chatMember.getMember().getMemberId());
        dto.setMemberName(chatMember.getMember().getMemberName());
        dto.setEmail(chatMember.getMember().getEmail());
        dto.setCompanyPhone(chatMember.getMember().getOfficePhone());
        dto.setPersonalPhone(chatMember.getMember().getMobilePhone());
        dto.setDepartment(chatMember.getMember().getDeptName());
        dto.setPosition(chatMember.getMember().getPosition());
        dto.setJoinAt(chatMember.getJoinAt());
        dto.setWithdrawalAt(chatMember.getMember().getWithdrawDate());
        dto.setActiveYn(chatMember.isActiveYn());
        return dto;
    }

    private MemberSerachDTO convertToMemberSearchDTO(Member member) {
        MemberSerachDTO dto = new MemberSerachDTO();
        dto.setMemberId(member.getMemberId());
        dto.setMemberName(member.getMemberName());
        dto.setEmail(member.getEmail());
        dto.setCompanyPhone(member.getOfficePhone());
        dto.setPersonalPhone(member.getMobilePhone());
        dto.setDepartment(member.getDeptName());
        dto.setPosition(member.getPosition());
        return dto;
    }

}
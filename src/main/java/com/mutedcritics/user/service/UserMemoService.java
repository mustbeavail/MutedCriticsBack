package com.mutedcritics.user.service;

import com.mutedcritics.dto.UserMemoRequestDTO;
import com.mutedcritics.dto.UserMemoResponseDTO;
import com.mutedcritics.entity.Member;
import com.mutedcritics.entity.User;
import com.mutedcritics.entity.UserMemo;
import com.mutedcritics.mail.repository.UserRepository;
import com.mutedcritics.member.repository.MemberRepository;
import com.mutedcritics.user.repository.UserMemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserMemoService {

    private final UserMemoRepository userMemoRepository;
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;

    // 메모 작성
    public void writeMemo(UserMemoRequestDTO request) {
        String userId = request.getUserId();
        String memberId = request.getMemberId();

        // 이미 작성한 메모가 있는지 확인
        boolean exists = userMemoRepository.existsByUserUserIdAndMemberMemberId(userId, memberId);
        if (exists) {
            throw new RuntimeException("이미 해당 유저에 대한 메모를 작성했습니다.");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("유저가 없습니다."));

        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new RuntimeException("회원이 없습니다."));

        UserMemo memo = new UserMemo();
        memo.setUser(user);
        memo.setMember(member);
        memo.setContent(request.getMemo());

        userMemoRepository.save(memo); // 메모 저장
    }

    // 메모 리스트 조회
    public List<UserMemoResponseDTO> getMemosByUser(String userId) {
        List<UserMemo> memoList = userMemoRepository.findByUserUserIdOrderByCreatedAtDesc(userId);
        return memoList.stream().map(memo -> UserMemoResponseDTO.builder() // @Builder 사용해서 엔티티 -> DTO 변환
                        .memoIdx(memo.getMemoIdx())
                        .memberId(memo.getMember().getMemberId())
                        .userId(memo.getUser().getUserId())
                        .memoContent(memo.getContent())
                        .createdAt(memo.getCreatedAt().toString())
                        .updatedAt(memo.getUpdatedAt().toString())
                        .build())
                .collect(Collectors.toList()); // list 로 반환
    }

    // 메모 수정 페이지
    public UserMemoResponseDTO updateMemoPage(int memoIdx, UserMemoRequestDTO request) {
        UserMemo memo = userMemoRepository.findById(memoIdx)
                .orElseThrow(() -> new RuntimeException("메모를 찾을 수 없습니다."));

        // 수정 페이지를 보려는 사람이 메모 작성자가 맞는지 체크
        String memoWriterId = memo.getMember().getMemberId();
        String requestMemberId = request.getMemberId();

        if (!memoWriterId.equals(requestMemberId)) {
            throw new RuntimeException("작성자만 수정할 수 있습니다.");
        }

        return UserMemoResponseDTO.builder()
                .memoIdx(memoIdx)
                .memberId(memoWriterId)
                .userId(memo.getUser().getUserId())
                .memoContent(memo.getContent())
                .createdAt(memo.getCreatedAt().toString())
                .updatedAt(memo.getUpdatedAt().toString())
                .build();
    }

    // 메모 수정
    @Transactional
    public void updateMemo(int memoIdx, UserMemoRequestDTO request) {
        UserMemo memo = userMemoRepository.findById(memoIdx)
                .orElseThrow(() -> new RuntimeException("메모를 찾을 수 없습니다."));

        // 수정하려는 사람이(memberId) 메모 작성자가 맞는지 체크
        String memoWriterId = memo.getMember().getMemberId();
        String requestMemberId = request.getMemberId();

        if (!memoWriterId.equals(requestMemberId)) {
            throw new RuntimeException("작성자만 수정할 수 있습니다.");
        }

        memo.setContent(request.getMemo());
    }

    // 메모 삭제
    @Transactional
    public void deleteMemo(int memoIdx, Map<String, Object> request) {
        UserMemo memo = userMemoRepository.findById(memoIdx)
                .orElseThrow(() -> new RuntimeException("메모를 찾을 수 없습니다."));

        // 삭제하려는 사람이(memberId) 메모 작성자가 맞는지 체크
        if (!memo.getMember().getMemberId().equals(request.get("memberId"))) {
            throw new RuntimeException("작성자만 삭제할 수 있습니다.");
        }

        userMemoRepository.deleteById(memoIdx);
    }

}

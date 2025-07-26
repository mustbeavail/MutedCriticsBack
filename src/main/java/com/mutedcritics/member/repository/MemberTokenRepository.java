package com.mutedcritics.member.repository;

import com.mutedcritics.entity.MemberToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberTokenRepository extends JpaRepository<MemberToken, String> {
    Optional<MemberToken> findByMemberId(String memberId);
}

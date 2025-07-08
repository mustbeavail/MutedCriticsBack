package com.mutedcritics.member;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mutedcritics.entity.Member;

public interface MemberRepository extends JpaRepository<Member, String> {}

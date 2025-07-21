package com.mutedcritics.member.repository;

import java.util.List;

import com.mutedcritics.entity.Member;

public interface MemberRepositoryCustom {

    List<Member> searchMembers(String keyword, String currentMemberId);
}
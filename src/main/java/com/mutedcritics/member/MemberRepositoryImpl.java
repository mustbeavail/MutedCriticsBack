package com.mutedcritics.member;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mutedcritics.entity.Member;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import static com.mutedcritics.entity.QMember.member;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    // 채팅 대상 회원 검색
    @Override
    public List<Member> searchMembers(String keyword, String currentMemberId) {
        BooleanBuilder builder = new BooleanBuilder();

        // 자신 제외
        builder.and(member.memberId.ne(currentMemberId));

        // 검색 조건
        if (keyword != null && !keyword.trim().isEmpty()) {
            BooleanBuilder searchBuilder = new BooleanBuilder();
            searchBuilder.or(member.memberName.containsIgnoreCase(keyword));
            searchBuilder.or(member.email.containsIgnoreCase(keyword));
            searchBuilder.or(member.deptName.containsIgnoreCase(keyword));
            searchBuilder.or(member.position.containsIgnoreCase(keyword));

            builder.and(searchBuilder);
        }

        return queryFactory.selectFrom(member)
                .where(builder)
                .orderBy(member.memberName.asc())
                .limit(20)
                .fetch();
    }
}
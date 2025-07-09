package com.mutedcritics.inquiry.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.mutedcritics.entity.Inquiry;
import com.mutedcritics.entity.QInquiry;
import com.mutedcritics.entity.QResponse;
import com.mutedcritics.entity.QUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class InquiryRepositoryImpl implements InquiryRepositoryCustom {

    private final JPAQueryFactory factory;

    @Override
    public Page<Inquiry> findInquiriesWithConditions(String userId, String category, String status, boolean isVip,
            String sortBy, String sortOrder, Pageable pageable) {

        QInquiry inquiry = QInquiry.inquiry;
        QUser user = QUser.user;

        BooleanBuilder builder = new BooleanBuilder();

        // type 이 '문의'인 것만 조회
        builder.and(inquiry.type.eq("문의"));

        // 유저 id 검색
        if (userId != null && !userId.isEmpty()) {
            builder.and(inquiry.user.userId.eq(userId));
        }

        // 카테고리 검색
        if (category != null && !category.isEmpty()) {
            builder.and(inquiry.category.eq(category));
        }

        // 상태 검색
        if (status != null && !status.isEmpty()) {
            builder.and(inquiry.status.eq(status));
        }

        // VIP 유저 필터링 (true 인 경우)
        if (isVip) {
            builder.and(inquiry.user.vipYn.eq(true));
        }

        // 정렬 처리
        JPAQuery<Inquiry> query = factory.selectFrom(inquiry)
                .leftJoin(inquiry.user, user).fetchJoin().where(builder);

        // 날짜 정렬
        if ("createdAt".equals(sortBy)) {
            if ("desc".equals(sortOrder)) {
                query.orderBy(inquiry.createdAt.desc());
            } else {
                query.orderBy(inquiry.createdAt.asc());
            }
        } else {
            // 기본 정렬 : 최신순
            query.orderBy(inquiry.createdAt.desc());
        }

        // 전체 개수 조회
        long totalCount = factory.select(inquiry.count())
                .from(inquiry).where(builder).fetchOne();

        // 페이징 적용
        List<Inquiry> content = query.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, totalCount);
    }

    @Override
    public Page<Inquiry> findByReportsWithConditions(String userId, String status, String sortBy, String sortOrder,
            Pageable pageable) {

        QInquiry inquiry = QInquiry.inquiry;
        QUser user = QUser.user;

        BooleanBuilder builder = new BooleanBuilder();

        // type 이 '신고'인 것만 조회
        builder.and(inquiry.type.eq("신고"));

        // 유저 id 검색
        if (userId != null && !userId.isEmpty()) {
            builder.and(inquiry.user.userId.eq(userId));
        }

        // 상태 검색
        if (status != null && !status.isEmpty()) {
            builder.and(inquiry.status.eq(status));
        }

        // 정렬 처리
        JPAQuery<Inquiry> query = factory.selectFrom(inquiry)
                .leftJoin(inquiry.user, user).fetchJoin().where(builder);

        // 날짜 정렬
        if ("createdAt".equals(sortBy)) {
            if ("desc".equals(sortOrder)) {
                query.orderBy(inquiry.createdAt.desc());
            } else {
                query.orderBy(inquiry.createdAt.asc());
            }
        } else {
            // 기본 정렬 : 최신순
            query.orderBy(inquiry.createdAt.desc());
        }

        // 전체 개수 조회
        long totalCount = factory.select(inquiry.count())
                .from(inquiry).where(builder).fetchOne();

        // 페이징 적용
        List<Inquiry> content = query.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, totalCount);

    }

    @Override
    public List<Inquiry> findUnansweredInquiries() {
        QInquiry inquiry = QInquiry.inquiry;
        QResponse response = QResponse.response;

        // 답변이 없는 문의/신고 조회(response 테이블이 null 인 경우)
        return factory.selectFrom(inquiry)
                .leftJoin(response).on(response.inquiry.eq(inquiry))
                .where(response.inquiry.isNull()) // response 테이블에 레코드가 없는 경우
                .orderBy(inquiry.createdAt.asc()) // 오래된 순서대로 처리
                .fetch();
    }
}
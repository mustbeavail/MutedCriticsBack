package com.mutedcritics.userStat.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.mutedcritics.dto.UserWinsRateDTO;
import com.mutedcritics.entity.QMatch;
import com.mutedcritics.entity.QMatchResult;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserStatRepositoryImpl implements UserStatRepositoryCustom {

    private final JPAQueryFactory factory;

    // 유저의 승리, 패배 횟수와 승률을 조회
    @Override
    public List<UserWinsRateDTO> getUserWinsRate(String userId, LocalDate startDate, LocalDate endDate) {
        QMatchResult mr = QMatchResult.matchResult;
        QMatch mt = QMatch.match;

        // 승리 횟수
        NumberExpression<Long> wins = new CaseBuilder()
                .when(mr.victoryOrDefeat.eq("victory")).then(1L)
                .otherwise(0L)
                .sum();

        // 패배 횟수
        NumberExpression<Long> losses = new CaseBuilder()
                .when(mr.victoryOrDefeat.eq("defeat")).then(1L)
                .otherwise(0L)
                .sum();

        // 승률 (소수점 1자리까지)
        NumberExpression<Double> winsRate = wins.doubleValue()
                .divide(mr.count().doubleValue())
                .multiply(100)
                .round();

        // 쿼리 생성
        JPAQuery<UserWinsRateDTO> query = factory
                .select(Projections.fields(UserWinsRateDTO.class,
                        mr.user.userId.as("userId"),
                        mr.count().as("totalMatches"),
                        wins.as("wins"),
                        losses.as("losses"),
                        winsRate.as("winsRate")))
                .from(mr)
                .join(mt).on(mr.match.matchIdx.eq(mt.matchIdx))
                .where(mt.matchDate.between(startDate, endDate))
                .groupBy(mr.user.userId);

        // userId 가 있으면 해당 유저만 조회
        if (userId != null) {
            query.where(mr.user.userId.eq(userId));
        }

        return query.fetch();
    }

}

package com.mutedcritics.userstat.dao;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mutedcritics.dto.HeroModeTimeDTO;
import com.mutedcritics.dto.UserDashBoardDTO;

/**
 * 유저 통계 관련 데이터베이스 접근을 담당하는 DAO 인터페이스.
 * MyBatis 매퍼와 연결됩니다.
 */
@Mapper // MyBatis 매퍼 인터페이스임을 나타냅니다.
public interface UserStatDAO {

    /**
     * 특정 유저의 요약 대시보드 정보를 데이터베이스에서 조회합니다.
     *
     * @param userId 조회할 유저 ID
     * @param startDate 조회 기간 시작일
     * @param endDate 조회 기간 종료일
     * @return 유저 대시보드 요약 정보 DTO
     */
    UserDashBoardDTO selectUsersummary(String userId, LocalDate startDate, LocalDate endDate);

    /**
     * 특정 유저의 영웅별 및 모드별 플레이타임 정보를 데이터베이스에서 조회합니다.
     *
     * @param userId 조회할 유저 ID
     * @param startDate 조회 기간 시작일
     * @param endDate 조회 기간 종료일
     * @return 영웅별 및 모드별 플레이타임 DTO 리스트
     */
    List<HeroModeTimeDTO> selectHeroModeTime(String userId, LocalDate startDate, LocalDate endDate);

}

package com.mutedcritics.notice.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mutedcritics.dto.NotiListDTO;
import com.mutedcritics.entity.Noti;

public interface NoticeRepository extends JpaRepository<Noti, Integer> {

    @Query("SELECT new com.mutedcritics.dto.NotiListDTO(" +
    "n.notiIdx, n.contentPre, n.relatedIdx, n.readYn, n.notiType, n.createdAt, " +
    "n.member.memberId, n.receiver.memberId, n.receiver.memberName) " +
    "FROM Noti n WHERE n.receiver.memberId = :memberId AND n.notiType = 'chat' AND n.readYn = false ORDER BY n.createdAt DESC")
    List<NotiListDTO> findAllByReceiverId(@Param("memberId") String memberId);

    @Query("SELECT n FROM Noti n WHERE n.notiType != 'chat' AND n.createdAt BETWEEN :firstDayOfMonth AND :lastDayOfMonth ORDER BY n.createdAt DESC")
    List<Noti> findAllByNotiType(
        @Param("firstDayOfMonth") LocalDateTime firstDayOfMonth,
        @Param("lastDayOfMonth") LocalDateTime lastDayOfMonth);

}

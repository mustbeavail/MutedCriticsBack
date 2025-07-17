package com.mutedcritics.notice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mutedcritics.entity.Noti;

public interface NoticeRepository extends JpaRepository<Noti, Integer> {

    @Query("SELECT n FROM Noti n WHERE n.receiver.memberId = :memberId AND n.notiType = 'chat' ORDER BY n.createdAt DESC")
    List<Noti> findAllByReceiverId(@Param("memberId") String memberId);

    @Query("SELECT n FROM Noti n WHERE n.notiType != 'chat' ORDER BY n.createdAt DESC")
    List<Noti> findAllByNotiType();

}

package com.mutedcritics.mail.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mutedcritics.entity.User;

public interface UserRepository extends JpaRepository<User, String> {

    // userType으로 userId 목록 조회
    @Query("SELECT u.userId FROM User u WHERE u.userType = :userType")
    List<String> findUserIdsByUserType(@Param("userType") String userType);
    
    // 또는 더 간단하게 메서드명으로도 가능
    List<String> findUserIdByUserType(String userType);
}

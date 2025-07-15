package com.mutedcritics.user.repository;

import com.mutedcritics.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserListRepository extends JpaRepository<User, String>, UserListRepositoryCustom {
}

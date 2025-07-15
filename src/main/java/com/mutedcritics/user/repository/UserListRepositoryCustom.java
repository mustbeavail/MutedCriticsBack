package com.mutedcritics.user.repository;

import com.mutedcritics.dto.UserListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserListRepositoryCustom {
    Page<UserListDTO> findUserList(String searchType, String keyword, Pageable pageable);
}

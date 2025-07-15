package com.mutedcritics.user.repository.impl;

import com.mutedcritics.dto.UserListDTO;
import com.mutedcritics.user.repository.UserListRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class UserListRepositoryImpl implements UserListRepositoryCustom {

    private final JPAQueryFactory factory;

    @Override
    public Page<UserListDTO> findUserList(String searchType, String keyword, Pageable pageable) {
        return null;
    }
}

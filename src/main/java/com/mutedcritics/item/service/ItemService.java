package com.mutedcritics.item.service;

import org.springframework.stereotype.Service;

import com.mutedcritics.item.dao.ItemDAO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {

    private final ItemDAO dao;
}

package com.mutedcritics.ingame.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.mutedcritics.ingame.dao.IngameDAO;

@Service
@RequiredArgsConstructor
public class IngameService {

    private final IngameDAO dao;

}

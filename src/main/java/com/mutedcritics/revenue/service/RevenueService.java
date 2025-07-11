package com.mutedcritics.revenue.service;

import org.springframework.stereotype.Service;

import com.mutedcritics.revenue.dao.RevenueDAO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RevenueService {

    private final RevenueDAO revenueDAO;
}

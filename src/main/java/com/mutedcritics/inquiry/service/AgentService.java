package com.mutedcritics.inquiry.service;

import org.springframework.stereotype.Service;

import com.mutedcritics.inquiry.repository.InquiryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AgentService {

    private final InquiryRepository repository;
}

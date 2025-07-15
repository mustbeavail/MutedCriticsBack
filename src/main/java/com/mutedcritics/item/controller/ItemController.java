package com.mutedcritics.item.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.mutedcritics.item.service.ItemService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin
@RestController
@Slf4j
@RequiredArgsConstructor
public class ItemController {

    private final ItemService service;

}

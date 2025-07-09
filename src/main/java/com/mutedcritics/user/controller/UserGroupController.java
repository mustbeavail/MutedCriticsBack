package com.mutedcritics.user.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.mutedcritics.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;




@CrossOrigin
@RestController
@Slf4j
@RequiredArgsConstructor
public class UserGroupController {

  private final UserService service;
  
  @GetMapping(value = "UserGroup/list")
  public Map<String,Object> UserGroupList(@RequestParam Map<String,Object> param) {
    

    
    return null;
  }
  

}

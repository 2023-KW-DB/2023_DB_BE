package com.dbdb.dbdb.domain.userlog.controller;

import com.dbdb.dbdb.domain.userlog.service.UserlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserlogController {

    @Autowired
    private UserlogService userlogService;
}

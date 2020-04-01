package com.robi.foodiy.controller.api;

import com.robi.data.ApiResult;
import com.robi.foodiy.service.UsersService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class UsersController {

    private static Logger logger = LoggerFactory.getLogger(UsersController.class);

    private UsersService usersSvc;

    @PostMapping("/users/validate")
    public ApiResult postAuthsValidate(
        @RequestHeader ("user_jwt") String userJwt
    ) {
        return usersSvc.checkUserStatus(userJwt);
    }
}
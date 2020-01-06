package com.robi.foodiy.controller;

import com.robi.data.ApiResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @GetMapping("/index")
    public ApiResult index() {
        return ApiResult.make(true, "0000", "Hello foodiy!");
    }
}
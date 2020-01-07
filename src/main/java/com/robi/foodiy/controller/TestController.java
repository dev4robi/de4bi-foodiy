package com.robi.foodiy.controller;

import com.robi.data.ApiResult;
import com.robi.foodiy.service.TestService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    private TestService testSvc;

    @GetMapping("/index")
    public ApiResult index() {
        return testSvc.index();
    }
}
package com.robi.foodiy.service;

import com.robi.data.ApiResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    private static final Logger logger = LoggerFactory.getLogger(TestService.class);

    public ApiResult index() {
        return ApiResult.make(true, null, "Hello Foodiy!");
    }
}
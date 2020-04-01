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

    // 인증서버 연동까지 완료.
    // 메인페이지에서 쿠키 저장이랑(장기/단기) 다른 페이지들 정상작동 확인부터 시작! @@ 
}
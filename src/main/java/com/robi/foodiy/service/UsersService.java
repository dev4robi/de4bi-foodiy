package com.robi.foodiy.service;

import com.robi.data.ApiResult;
import com.robi.util.MapUtil;
import com.robi.util.RestHttpUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UsersService {

    private final Logger logger = LoggerFactory.getLogger(UsersService.class);

    // 회원 존재여부, 서비스 접근유효성등 검사
    public ApiResult checkUserStatus(String userJwt) {
        HttpHeaders httpHeader = new HttpHeaders();
        httpHeader.setContentType(MediaType.APPLICATION_JSON);

        JSONObject postJsonObj = null;
        
        try {
            postJsonObj = new JSONObject();
            postJsonObj.put("userJwt", userJwt);
        }
        catch (JSONException e) {
            logger.error("Exception!", e);
            return ApiResult.make(false);
        }

        String responseStr = RestHttpUtil.urlConnection("http://dev4robi.net:40000/users/api/jwt/validate",
                                                        RestHttpUtil.METHOD_POST,
                                                        MapUtil.toMap("Content-Type", "application/json;charset=utf-8"),
                                                        MapUtil.toMap("userJwt", userJwt, "audience", "foodiy"));
        JSONObject rpyObj = null;
        ApiResult rpyApiRst = null;
        
        try {
            rpyObj = new JSONObject(responseStr);
            rpyApiRst = ApiResult.make(rpyObj);
        }
        catch (JSONException e) {
            logger.error("Exception!", e);
            return ApiResult.make(false, "10000"); // 회원관련 오류가 발생했습니다.
        }

        if (!rpyApiRst.getResult()) {
            return rpyApiRst;
        }

        return rpyApiRst;
    }
}
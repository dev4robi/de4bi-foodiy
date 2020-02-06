package com.robi.foodiy.service;

import com.robi.data.ApiResult;
import com.robi.foodiy.data.dao.RecordsDao;
import com.robi.foodiy.mapper.RecordsMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class RecordsWithMenusService {

    private static final Logger logger = LoggerFactory.getLogger(RecordsWithMenusService.class);

    RecordsMapper recordsMapper;

    /**
     * 
     * @param id
     * @return 
     */
    public ApiResult selectRecordById(String userJwt, long id) {
        return null;
    }

    /**
     * <p>userJwt내의 write_user_id에 해당하는 List<{@link RecordsDao}>를 반환합니다.</p>
     * @param userJwt : 유저 토큰
     * @return {@link ApiResult}내의 ({@link RecordsDao} selectedRecords)
     */
    public ApiResult selectRecordsByWriteUserId(String userJwt) {
        return null;
    }

    // 여기부터 시작...
    // 껍데기 다 만들어 놓고 DB연동과 웹에서 전송하는 테스트 해보자
    // 1. 이후로는 이미지 업로드시, 확장자 및 크기조정
    // 2. 인증서버 연동
    // 3. 아마존 S3연동 정도...?
    public ApiResult insertRecordsWithMenus(String userJwt, RecordsDao recordsDao) {
        return null;
    }

    public ApiResult updateRecordsById(String userJwt, RecordsDao recordsDao) {
        return null;
    }

    public ApiResult deleteRecordsById(String userJwt, long id) {
        return null;
    }
}
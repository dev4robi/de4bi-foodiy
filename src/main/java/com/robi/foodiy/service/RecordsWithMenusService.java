package com.robi.foodiy.service;

import java.util.List;

import com.robi.data.ApiResult;
import com.robi.foodiy.data.dao.RecordsDao;
import com.robi.foodiy.mapper.RecordsMapper;
import com.robi.util.MapUtil;
import com.robi.util.ValidatorUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class RecordsWithMenusService {

    private static final Logger logger = LoggerFactory.getLogger(RecordsWithMenusService.class);

    UsersService usersService;      // Users 회원인증을 위한 서비스
    RecordsMapper recordsMapper;    // Records DB접근 매퍼

    /**
     * <p>userJwt의 user_id값과 write_user_id값이 일치하는 경우, id값을 가진 {@link RecordsDao}를 반환합니다.</p>
     * @param userJwt : auth서버로부터 발급된 JWT
     * @param id : 조회할 records의 id값
     * @return ({@link RecordsDao}) ApiResult.getData("selectedRecord");
     */
    public ApiResult selectRecordById(String userJwt, long id) {
        // 파라미터 검사
        ApiResult validResult = null;

        if (!(validResult = ValidatorUtil.nullOrZeroLen("userJwt", userJwt)).getResult()) {
            logger.error(validResult.getResultMsg());
            return ApiResult.make(false, ApiResult.DEFAULT_API_RESULT_CODE_NEGATIVE, validResult.getResultMsg());
        }

        // 사용자 인증
        ApiResult userAuthResult = usersService.checkUserStatus(userJwt);

        if (userAuthResult == null || userAuthResult.getResult() == false) {
            logger.error("Fail to auth user! (userJwt: " + userJwt + ")");
            return userAuthResult;
        }

        // DB조회 (RecordsMapper.xml)
        long writerId = Long.valueOf(userAuthResult.getDataAsStr("id"));
        RecordsDao selectedRecord = null;

        try {
            selectedRecord = recordsMapper.selectById(id);
        }
        catch (Exception e) {
            logger.error("RecordsDB Exception!", e);
        }

        if (selectedRecord != null && selectedRecord.getWriteUserId() != writerId) {
            // 작성자가 아닌 경우 조회실패
            logger.error("write_user_id != writerId (writer_user_id: " + selectedRecord.getWriteUserId() +
                         "writerId: " + writerId + ")");
            selectedRecord = null;
        }

        // 결과 반환
        if (selectedRecord == null) {
            logger.error("저장된 기록 찾기에 실패했습니다.");
            ApiResult.make(false, ApiResult.DEFAULT_API_RESULT_CODE_NEGATIVE, "저장된 기록 찾기에 실패했습니다.");
        }

        return ApiResult.make(true, MapUtil.toMap("selectedRecord", selectedRecord));
    }

    /**
     * <p>userJwt의 user_id값과 write_user_id값이 일치하는 값들을 List<{@link RecordsDao}>로 반환합니다.</p>
     * @param userJwt : 유저 토큰
     * @return ({@link RecordsDao}) ApiResult.getData("selectedRecordList");
     */
    public ApiResult selectRecordAllByWriteUserId(String userJwt) {
        // 파라미터 검사
        ApiResult validResult = null;

        if (!(validResult = ValidatorUtil.nullOrZeroLen("userJwt", userJwt)).getResult()) {
            logger.error(validResult.getResultMsg());
            return ApiResult.make(false, ApiResult.DEFAULT_API_RESULT_CODE_NEGATIVE, validResult.getResultMsg());
        }

        // 사용자 인증
        ApiResult userAuthResult = usersService.checkUserStatus(userJwt);

        if (userAuthResult == null || userAuthResult.getResult() == false) {
            logger.error("Fail to auth user! (userJwt: " + userJwt + ")");
            return userAuthResult;
        }

        // DB조회 (RecordsMapper.xml)
        long writerId = Long.valueOf(userAuthResult.getDataAsStr("id"));
        List<RecordsDao> selectedRecordList = null;
        
        try {
            selectedRecordList = recordsMapper.selectAllByWriteUserId(writerId);
        }
        catch (Exception e) {
            logger.error("RecordsDB Exception!", e);
        }

        // 결과검사
        if (selectedRecordList == null ||  selectedRecordList.size() == 0) {
            logger.error("Fail to find records.");
            return ApiResult.make(false, ApiResult.DEFAULT_API_RESULT_CODE_NEGATIVE, "저장된 기록 찾기에 실패했습니다.");
        }

        // 성공응답
        return ApiResult.make(true, MapUtil.toMap("selectedRecordList", selectedRecordList));
    }

    /**
     * <p>userJwt내의 user_id를 write_user_id로 하여 record추가합니다.</p>
     * @param userJwt : auth서버로부터 발급된 JWT
     * @param recordsDao : 추가할 record데이터
     * @return ApiResult
     */
    public ApiResult insertRecordsWithMenus(String userJwt, RecordsDao recordsDao) {
        // 파라미터 검사
        ApiResult validResult = null;

        if (!(validResult = ValidatorUtil.nullOrZeroLen("userJwt", userJwt)).getResult()) {
            logger.error(validResult.getResultMsg());
            return ApiResult.make(false, ApiResult.DEFAULT_API_RESULT_CODE_NEGATIVE, validResult.getResultMsg());
        }

        if (recordsDao == null) {
            logger.error("recordsDao == null");
            return ApiResult.make(false, ApiResult.DEFAULT_API_RESULT_CODE_NEGATIVE, "recordsDao == null");
        }

        // 사용자 인증
        ApiResult userAuthResult = usersService.checkUserStatus(userJwt);

        if (userAuthResult == null || userAuthResult.getResult() == false) {
            logger.error("Fail to auth user! (userJwt: " + userJwt + ")");
            return userAuthResult;
        }

        // RecordsDao 수정
        long writeUserId = Long.valueOf(userAuthResult.getDataAsStr("id"));
        recordsDao.setWriteUserId(writeUserId);

        // DB에 추가 (RecordsMapper.xml)
        try {
            recordsMapper.insert(recordsDao);
        }
        catch (Exception e) {
            logger.error("RecordsDB insert Exception!", e);
            return ApiResult.make(false, ApiResult.DEFAULT_API_RESULT_CODE_NEGATIVE, "기록 추가에 실패했습니다.");
        }

        logger.info("RecordsDB insert success! (recordsDao: " + recordsDao.toString() + ")");
        return ApiResult.make(true);
    }

    /**
     * <p>userJwt내의 user_id를 write_user_id로 하여 record갱신합니다.</p>
     * @param userJwt : auth서버로부터 발급된 JWT
     * @param recordsDao : 갱신할 record데이터
     * @return ApiResult
     */
    public ApiResult updateRecordsById(String userJwt, RecordsDao recordsDao) {
        // 파라미터 검사
        ApiResult validResult = null;

        if (!(validResult = ValidatorUtil.nullOrZeroLen("userJwt", userJwt)).getResult()) {
            logger.error(validResult.getResultMsg());
            return ApiResult.make(false, ApiResult.DEFAULT_API_RESULT_CODE_NEGATIVE, validResult.getResultMsg());
        }

        if (recordsDao == null) {
            logger.error("recordsDao == null");
            return ApiResult.make(false, ApiResult.DEFAULT_API_RESULT_CODE_NEGATIVE, "recordsDao == null");
        }

        // 사용자 인증
        ApiResult userAuthResult = usersService.checkUserStatus(userJwt);

        if (userAuthResult == null || userAuthResult.getResult() == false) {
            logger.error("Fail to auth user! (userJwt: " + userJwt + ")");
            return userAuthResult;
        }

        // RecordsDao 수정
        long writeUserId = Long.valueOf(userAuthResult.getDataAsStr("id"));
        recordsDao.setWriteUserId(writeUserId);

        // DB데이터 수정 (RecordsMapper.xml)
        try {
            recordsMapper.update(recordsDao);
        }
        catch (Exception e) {
            logger.error("RecordsDB update Exception!", e);
            return ApiResult.make(false, ApiResult.DEFAULT_API_RESULT_CODE_NEGATIVE, "기록 수정에 실패했습니다.");
        }

        logger.info("RecordsDB update success! (recordsDao: " + recordsDao.toString() + ")");
        return ApiResult.make(true);
    }

    /**
     * <p>userJwt의 user_id값과 write_user_id값이 일치하는 경우, id값을 가진 {@link RecordsDao}를 삭제합니다.</p>
     * @param userJwt : auth서버로부터 발급된 JWT
     * @param id : 삭제할 records의 id값
     * @return ApiResult
     */
    @Transactional
    public ApiResult deleteRecordsById(String userJwt, long id) {
        // 파라미터 검사
        ApiResult validResult = null;

        if (!(validResult = ValidatorUtil.nullOrZeroLen("userJwt", userJwt)).getResult()) {
            logger.error(validResult.getResultMsg());
            return ApiResult.make(false, ApiResult.DEFAULT_API_RESULT_CODE_NEGATIVE, validResult.getResultMsg());
        }

        // 사용자 인증및 기록 조회
        ApiResult selectResult = selectRecordById(userJwt, id);

        if (selectResult == null || !selectResult.getResult()) {
            return selectResult;
        }

        RecordsDao selectedDao = (RecordsDao) selectResult.getData("selectedRecord");

        if (selectedDao == null) {
            return ApiResult.make(false, ApiResult.DEFAULT_API_RESULT_CODE_NEGATIVE, "원본 기록 찾기 실패! 삭제에 실패했습니다.");
        }

        // DB데이터 삭제 (RecordsMapper.xml)
        try {
            recordsMapper.deleteById(id);
        }
        catch (Exception e) {
            logger.error("RecordsDB delete Exception!", e);
            return ApiResult.make(false, ApiResult.DEFAULT_API_RESULT_CODE_NEGATIVE, "기록 삭제에 실패했습니다.");
        }

        logger.info("RecordsDB delete success! (id: " + id + ")");
        return ApiResult.make(true);
    }
}

// 메서드 테스트부터 수행...
// 이후, 이미지 업로드하는 부분 작업... @@
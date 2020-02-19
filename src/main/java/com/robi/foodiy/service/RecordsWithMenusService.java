package com.robi.foodiy.service;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.robi.data.ApiResult;
import com.robi.foodiy.data.dao.MenusDao;
import com.robi.foodiy.data.dao.RecordsDao;
import com.robi.foodiy.data.dto.PostRecordsDto;
import com.robi.foodiy.mapper.MenusMapper;
import com.robi.foodiy.mapper.RecordsMapper;
import com.robi.util.MapUtil;
import com.robi.util.MdUtil;
import com.robi.util.RestHttpUtil;
import com.robi.util.ValidatorUtil;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;

@PropertySource("classpath:config.properties")
@AllArgsConstructor
@Service
public class RecordsWithMenusService {

    private static final Logger logger = LoggerFactory.getLogger(RecordsWithMenusService.class);

    UsersService usersService;      // Users 회원인증을 위한 서비스
    RecordsMapper recordsMapper;    // Records DB접근 매퍼
    MenusMapper menusMapper;        // Menus DB접근 매퍼
    Environment env;                // config.properties

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
            return ApiResult.make(false, ApiResult.DEFAULT_API_RESULT_CODE_NEGATIVE, "저장된 기록 찾기에 실패했습니다.");
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
     * <p>(※순서: 파라미터 검사 -> 사용자 인증 -> Google Geocoding API -> 이미지 저장 -> RecordsDao생성 ->
     * Records DB Insert -> MenusDao생성 -> MenusDao DB Insert -> 결과 반환)</p>
     * @param userJwt : auth서버로부터 발급된 JWT
     * @param recordsDto : 추가할 record데이터
     * @return ApiResult
     */
    public ApiResult insertRecordsWithMenus(String userJwt, PostRecordsDto recordsDto) {
        // 파라미터 검사
        ApiResult validResult = null;

        if (!(validResult = ValidatorUtil.nullOrZeroLen("userJwt", userJwt)).getResult()) {
            logger.error(validResult.getResultMsg());
            return ApiResult.make(false, ApiResult.DEFAULT_API_RESULT_CODE_NEGATIVE, validResult.getResultMsg());
        }

        if (recordsDto == null) {
            logger.error("recordsDto == null");
            return ApiResult.make(false, ApiResult.DEFAULT_API_RESULT_CODE_NEGATIVE, "recordsDto == null");
        }

        // 사용자 인증
        ApiResult userAuthResult = usersService.checkUserStatus(userJwt);

        if (userAuthResult == null || userAuthResult.getResult() == false) {
            logger.error("Fail to auth user! (userJwt: " + userJwt + ")");
            return userAuthResult;
        }

        // Google Geocoding API
        String googleRpyStr = RestHttpUtil.urlConnection("https://maps.googleapis.com/maps/api/geocode/json?latlng=" + 
            recordsDto.getWhereLati() + "," + recordsDto.getWhereLongi() + 
            "&key=AIzaSyDTxpWl_A8b_V9e2lktHYgJg1HYmfyzLhM", 
            RestHttpUtil.METHOD_GET, null, null);
        String wherePlaceName = "GOOGLE-API-RESULT-HERE";

        // 이미지 저장
        MultipartFile[] recordsPic = recordsDto.getPics();
        StringBuilder fileUrlSb = new StringBuilder(128);
        if (recordsPic != null) {
            for (int i = 0; i < recordsPic.length; ++i) {
                try {
                    MultipartFile picFile = recordsPic[i];
                    String originFileName = picFile.getOriginalFilename();
                    String fileDir = env.getProperty("foodiy.records.img.basedir");
                    String fileName = Hex.encodeHexString(MdUtil.sha128((originFileName + System.currentTimeMillis()).getBytes()), true);
                    String fileExt = originFileName.substring(originFileName.lastIndexOf("."), originFileName.length());
                    String filePath = fileDir + "/" + fileName + fileExt;
                    fileUrlSb.append(filePath + "`");
                    File recordPicDir = new File(fileDir);
                    if (!recordPicDir.exists()) {
                        boolean rst = recordPicDir.mkdirs();
                        logger.debug("rst: " + rst);
                    }
                    File recordPicFile = new File(filePath);
                    picFile.transferTo(recordPicFile);
                }
                catch (IOException | IllegalStateException e) {
                    logger.warn("Exception while store records img file!", e);
                    continue;
                }
            }
        }

        fileUrlSb.setLength(fileUrlSb.length() - 1);

        // 여기부터 시작... 구글 geocoding api는 공인 ip 환경에서만 테스트 가능...
        // 정상데이터 응답받은 경우 파싱하여 set 하는 부분까지만 작업해놓고,
        // 실패할 경우에는 빈 문자를 채워넣는 방식으로 접근... (로컬에선 무조건 실패니까!)
        // RecordsDao 인서트까지 정상적이고, MenusDao 생성하여 인서트하는부분 작업하면 될 듯!
        // 조금 만 더 화이팅합시다... !! @@

        // RecordsDao 생성
        String whenDate = recordsDto.getWhenDate();
        String whenTime = recordsDto.getWhenTime();
        Calendar dateTimeCal = Calendar.getInstance();
        dateTimeCal.set(
            Integer.valueOf(whenDate.substring(0, 4)),  // year
            Integer.valueOf(whenDate.substring(4, 6)),  // month
            Integer.valueOf(whenDate.substring(6, 8)),  // date
            Integer.valueOf(whenTime.substring(0, 2)),  // hoursOfDay
            Integer.valueOf(whenTime.substring(2, 4)),  // minute
            Integer.valueOf(whenTime.substring(4, 6))   // second
        );
        
        long dateTimeMs = dateTimeCal.getTime().getTime();
        long writeUserId = Long.valueOf(userAuthResult.getDataAsStr("id"));
        
        RecordsDao recordsDao = new RecordsDao();
        recordsDao.setWriteUserId(writeUserId);
        recordsDao.setTitle(recordsDto.getTitle());
        recordsDao.setWhenDate(new Date(dateTimeMs));
        recordsDao.setWhenTime(new Time(dateTimeMs));
        recordsDao.setWherePlace(wherePlaceName);
        recordsDao.setWhereLati(recordsDto.getWhereLati());
        recordsDao.setWhereLongi(recordsDto.getWhereLongi());
        recordsDao.setWhoWith(recordsDto.getWhoWith());
        recordsDao.setPicUrls(fileUrlSb.toString());

        // DB에 추가 (RecordsMapper.xml)
        try {
            recordsMapper.insert(recordsDao);
        }
        catch (Exception e) {
            logger.error("RecordsDB insert Exception!", e);
            return ApiResult.make(false, ApiResult.DEFAULT_API_RESULT_CODE_NEGATIVE, "기록 추가에 실패했습니다.");
        }

        // MenusDao 생성
        MenusDao[] menusDao = null;
        // ...

        // DB에 추가 (MenusMapper.xml)
        try {
            for (int i = 0; i < menusDao.length; ++i) {
                menusMapper.insert(menusDao[i]);
            }
        }
        catch (Exception e) {
            logger.error("MenusDB insert Exception!", e);
            return ApiResult.make(false, ApiResult.DEFAULT_API_RESULT_CODE_NEGATIVE, "메뉴 추가에 실패했습니다.");
        }

        logger.info("Records & MenusDB insert success! (recordsDao: " + recordsDao.toString() + " / " + Arrays.toString(menusDao) + ")");
        return ApiResult.make(true);
    }

    /**
     * <p>userJwt내의 user_id를 write_user_id로 하여 record를갱신합니다.</p>
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
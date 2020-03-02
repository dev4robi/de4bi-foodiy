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
import com.robi.foodiy.data.dto.PostMenusDto;
import com.robi.foodiy.data.dto.PostRecordsDto;
import com.robi.foodiy.mapper.MenusMapper;
import com.robi.foodiy.mapper.RecordsMapper;
import com.robi.util.MapUtil;
import com.robi.util.MdUtil;
import com.robi.util.RestHttpUtil;
import com.robi.util.StorageUtil;
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
            selectedRecord = recordsMapper.selectById(id); // 조회 결과, getWriteUserId 값이 0으로 채워져서 나온다... 이유는? @@ 여기부터 시작 @@
        }
        catch (Exception e) {
            logger.error("RecordsDB Exception!", e);
        }

        if (selectedRecord != null && selectedRecord.getWriteUserId() != writerId) {
            // 작성자가 아닌 경우 조회실패
            logger.error("write_user_id != writerId (writer_user_id: " + selectedRecord.getWriteUserId() +
                         ", writerId: " + writerId + ")");
            selectedRecord = null;
        }

        // 결과 반환
        if (selectedRecord == null) {
            logger.error("저장된 기록 찾기에 실패했습니다.");
            return ApiResult.make(false, ApiResult.DEFAULT_API_RESULT_CODE_NEGATIVE, "저장된 기록 찾기에 실패했습니다.");
        }

        return ApiResult.make(true, MapUtil.toMap("selectedRecord", selectedRecord));
    }

/*
    // TEST
    curl -i -X POST \
   -H "user_jwt:-CkcWpJaYYF5ThH0UkXadT8b6AOTQQH3-6BC9kjjGTqQCYj4TRbjpye3AJJKUL9ZLowwVkA8bgs6u8YVQjPeGtNXoOqMcXKWkmQsFRJIG-xp4GD9maPe5iEuF2nWs27AHvXAskMVkMFE8WqVPZqSDFuyTJcEGlEqWDc7-Yhn7mxvRf2roCLLJXvZFYgBPmwGGz4xr_sa9RxjPIR7kdyIpPIz2sVLGzeYHDbChJNX2zWX-utaZblUH979uXmgMfcbKDZ9GJhxQxXwc1oOhOLBqyX-sBF5Yy7nOTf8R3G9Nu1wFUWj3Ur6IdoVon_Uua9FNkUiOqO-ob7jAQCoDuJj5HH2DFC6EfwTXW1jRAUTg4PYIdZ75jwWX3hSaz76Let_" \
   -H "Content-Type:multipart/form-data" \
   -F "title=hello title!" \
   -F "when_date=20200212" \
   -F "when_time=220030" \
   -F "where_lati=37.576824" \
   -F "where_longi=127.050757" \
   -F "who_with=me alone" \
   -F "where_place=서울시 어디" \
   -F "pics=@\"./R1.png\";type=image/png;filename=\"R1.png\"" \
   -F "pics=@\"./R2.png\";type=image/png;filename=\"R2.png\"" \
   -F "menus=[[\"m1\",\"10000\",\"#존맛탱\",\"5\",\"1\"],[\"m2\",\"1000\",\"#노맛\",\"1\",\"1\"]]" \
   -F "menu_pics=@\"./M1.png\";type=image/png;filename=\"M1.png\"" \
   -F "menu_pics=@\"./M2.png\";type=image/png;filename=\"M2.png\"" \
 'http://localhost:40003/api/records'
 */
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
    public ApiResult insertRecordsWithMenus(String userJwt, PostRecordsDto recordsDto, PostMenusDto[] menusDtoAry) {
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

        // Google Geocoding API (추후 작업)
        String googleRpyStr = RestHttpUtil.urlConnection("https://maps.googleapis.com/maps/api/geocode/json?latlng=" + 
            recordsDto.getWhereLati() + "," + recordsDto.getWhereLongi() + 
            "&key=AIzaSyDTxpWl_A8b_V9e2lktHYgJg1HYmfyzLhM", 
            RestHttpUtil.METHOD_GET, null, null);
        String wherePlaceName = "GOOGLE-API-RESULT-HERE"; // <-- 여기 작업

        // 레코드
        // 기록 이미지 저장
        StringBuilder fileUrlSb = new StringBuilder(256);
        MultipartFile[] recordsPic = recordsDto.getPics();
        String rFileDir = env.getProperty("foodiy.records.img.basedir") + "/records";
        
        for (int mPicIdx = 0; mPicIdx < recordsPic.length; ++mPicIdx) {
            try {
                MultipartFile rMpFile = recordsPic[mPicIdx];
                String rOriName = rMpFile.getOriginalFilename();
                String fileExt = rOriName.substring(rOriName.lastIndexOf("."), rOriName.length());
                String fileName = Hex.encodeHexString(MdUtil.sha128((rOriName + System.currentTimeMillis()).getBytes()), true) + fileExt;
                String fileUrl =  StorageUtil.storeMultipartAsFile(recordsPic[mPicIdx], rFileDir, fileName);
                fileUrlSb.append(fileUrl != null ? fileUrl : "").append("`");
            }
            catch (NullPointerException | IndexOutOfBoundsException | IllegalArgumentException | IOException e) {
                logger.warn("Exception while storing record img!", e);
                continue;
            }
        }

        if (fileUrlSb.length() > 0) {
            fileUrlSb.setLength(fileUrlSb.length() - 1);
        }

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
        
        final long dateTimeMs = dateTimeCal.getTime().getTime();
        final long writeUserId = Long.valueOf(userAuthResult.getDataAsStr("id"));
        
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

        // 메뉴
        MenusDao menusDao = null;

        for (int menuIdx = 0; menuIdx < menusDtoAry.length; ++menuIdx) {
            // 메뉴 이미지 저장    
            PostMenusDto menusDto = menusDtoAry[menuIdx];
            fileUrlSb.setLength(0);
            MultipartFile[] menusPic = menusDto.getMenuPics();
            String mFileDir = env.getProperty("foodiy.records.img.basedir") + "/menus";
            
            for (int mPicIdx = 0; mPicIdx < menusPic.length; ++mPicIdx) {
                try {
                    MultipartFile mMpFile = menusPic[mPicIdx];
                    String mOriName = mMpFile.getOriginalFilename();
                    String fileExt = mOriName.substring(mOriName.lastIndexOf("."), mOriName.length());
                    String fileName = Hex.encodeHexString(MdUtil.sha128((mOriName + System.currentTimeMillis()).getBytes()), true) + fileExt;
                    String fileUrl =  StorageUtil.storeMultipartAsFile(menusPic[mPicIdx], mFileDir, fileName);
                    fileUrlSb.append(fileUrl != null ? fileUrl : "").append("`");
                }
                catch (NullPointerException | IndexOutOfBoundsException | IllegalArgumentException | IOException e) {
                    logger.warn("Exception while storing menus img!", e);
                    continue;
                }
            }
    
            if (fileUrlSb.length() > 0) {
                fileUrlSb.setLength(fileUrlSb.length() - 1);
            }

            // MenusDao 생성
            menusDao = new MenusDao();
            menusDao.setName(menusDto.getMenuName());
            menusDao.setWriteUserId(writeUserId);
            menusDao.setRecordId(recordsDao.getId());
            menusDao.setPicUrl(fileUrlSb.toString());
            menusDao.setPrice(Integer.valueOf(menusDto.getMenuPrice()));
            menusDao.setTags(menusDto.getMenuTag());
            menusDao.setScore(Integer.valueOf(menusDto.getMenuScore()));

            // DB에 추가 (MenusMapper.xml)
            try {
                menusMapper.insert(menusDao);
            }
            catch (Exception e) {
                logger.error("MenusDB insert Exception!", e);
                return ApiResult.make(false, ApiResult.DEFAULT_API_RESULT_CODE_NEGATIVE, "메뉴 추가에 실패했습니다.");
            }
        }

        logger.info("Records & MenusDB insert success! (recordsDao: " + recordsDao.toString() + " / " + menusDao.toString() + ")");
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
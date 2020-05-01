package com.robi.foodiy.service;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

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
import org.springframework.web.client.RestClientException;
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

        final long writeUserId = Long.valueOf(userAuthResult.getDataAsStr("id"));

        // Google Geocoding API (추후 작업)
        String googleRpyStr = null;
        
        try {
            googleRpyStr = RestHttpUtil.urlConnection("https://maps.googleapis.com/maps/api/geocode/json?latlng=" + 
                recordsDto.getWhereLati() + "," + recordsDto.getWhereLongi() + 
                "&key=AIzaSyDTxpWl_A8b_V9e2lktHYgJg1HYmfyzLhM", 
                RestHttpUtil.METHOD_GET, null, null);
        }
        catch (RestClientException e) {
            logger.warn("Exception while geocoding API!", e);
        }

        String wherePlaceName = "GOOGLE-API-RESULT-HERE"; // <-- 여기 작업

        // 레코드
        // 기록 이미지 저장
        StringBuilder fileUrlSb = new StringBuilder(256);
        MultipartFile[] recordsPic = recordsDto.getPics();
        String rFileDir = env.getProperty("foodiy.records.img.basedir");
        Calendar todayCal = Calendar.getInstance();
        final String filePrefix = new SimpleDateFormat("yyyyMMddHHmmss").format(todayCal.getTime()) + "-" + writeUserId;
        
        for (int mPicIdx = 0; mPicIdx < recordsPic.length; ++mPicIdx) {
            try {
                MultipartFile rMpFile = recordsPic[mPicIdx];
                String rOriName = rMpFile.getOriginalFilename();
                String fileExt = rOriName.substring(rOriName.lastIndexOf("."), rOriName.length());
                String fileName = filePrefix + "-" +Hex.encodeHexString(MdUtil.sha128((rOriName + System.currentTimeMillis()).getBytes()), true) + fileExt;
                StorageUtil.storeMultipartAsFile(recordsPic[mPicIdx], rFileDir, fileName);
                fileUrlSb.append(fileName != null ? fileName : "").append("`");
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
            Integer.valueOf(whenDate.substring(0, 4)),      // year
            Integer.valueOf(whenDate.substring(4, 6)) - 1,  // month
            Integer.valueOf(whenDate.substring(6, 8)),      // date
            Integer.valueOf(whenTime.substring(0, 2)),      // hoursOfDay
            Integer.valueOf(whenTime.substring(2, 4)),      // minute
            Integer.valueOf(whenTime.substring(4, 6))       // second
        );
        
        final long dateTimeMs = dateTimeCal.getTime().getTime();
        
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
            String mFileDir = env.getProperty("foodiy.records.img.basedir");
            
            for (int mPicIdx = 0; mPicIdx < menusPic.length; ++mPicIdx) {
                try {
                    MultipartFile mMpFile = menusPic[mPicIdx];
                    String rOriName = mMpFile.getOriginalFilename();
                    String fileExt = rOriName.substring(rOriName.lastIndexOf("."), rOriName.length());
                    String fileName = filePrefix + "-" + Hex.encodeHexString(MdUtil.sha128((rOriName + System.currentTimeMillis()).getBytes()), true) + fileExt;
                    StorageUtil.storeMultipartAsFile(menusPic[mPicIdx], mFileDir, fileName);
                    fileUrlSb.append(fileName != null ? fileName : "").append("`");
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
            String menuName = menusDto.getMenuName();

            if (menuName != null && menuName.length() > 0) {
                menusDao = new MenusDao();
                menusDao.setName(menuName);
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
        }

        String paramStr = ("recordsDao: " + recordsDao.toString());
        
        if (menusDao != null) {
            paramStr += (" / " + menusDao.toString() + ")");
        }

        logger.info("Records & MenusDB insert success! " + paramStr);
        return ApiResult.make(true);
    }

    /**
     * <p>userJwt내의 user_id를 write_user_id로 하여 record를갱신합니다.</p>
     * @param userJwt : auth서버로부터 발급된 JWT
     * @param recordsDao : 갱신할 record데이터
     * @return ApiResult
     */
    public ApiResult updateRecordsById(String userJwt, PostRecordsDto postRecordsDto) {
        // 파라미터 검사
        ApiResult validResult = null;

        if (!(validResult = ValidatorUtil.nullOrZeroLen("userJwt", userJwt)).getResult()) {
            logger.error(validResult.getResultMsg());
            return ApiResult.make(false, ApiResult.DEFAULT_API_RESULT_CODE_NEGATIVE, validResult.getResultMsg());
        }

        if (postRecordsDto == null) {
            logger.error("postRecordsDto == null");
            return ApiResult.make(false, ApiResult.DEFAULT_API_RESULT_CODE_NEGATIVE, "postRecordsDto == null");
        }

        // 사용자 인증
        ApiResult userAuthResult = usersService.checkUserStatus(userJwt);

        if (userAuthResult == null || userAuthResult.getResult() == false) {
            logger.error("Fail to auth user! (userJwt: " + userJwt + ")");
            return userAuthResult;
        }

        long writeUserId = Long.valueOf(userAuthResult.getDataAsStr("id"));

        // DB데이터 조회 (RecordsMapper.xml)
        RecordsDao selectedRecordsDao = null;

        try {
            selectedRecordsDao = recordsMapper.selectById(postRecordsDto.getId());
        }
        catch (Exception e) {
            logger.error("RecordsDB update Exception!", e);
            return ApiResult.make(false, ApiResult.DEFAULT_API_RESULT_CODE_NEGATIVE, "기록 수정에 실패했습니다.");
        }

        if (selectedRecordsDao != null && selectedRecordsDao.getWriteUserId() != writeUserId) {
            // 작성자가 아닌 경우 조회실패
            logger.error("write_user_id != writerId (writer_user_id: " + selectedRecordsDao.getWriteUserId() +
                         ", writerId: " + writeUserId + ")");
                         selectedRecordsDao = null;
        }

        // 결과 반환
        if (selectedRecordsDao == null) {
            logger.error("저장된 기록 찾기에 실패했습니다.");
            return ApiResult.make(false, ApiResult.DEFAULT_API_RESULT_CODE_NEGATIVE, "저장된 기록 찾기에 실패했습니다.");
        }

        // RecordsDao 수정
        RecordsDao recordsDao = new RecordsDao();
        String whenDate = postRecordsDto.getWhenDate();
        String whenTime = postRecordsDto.getWhenTime();
        Calendar dateTimeCal = Calendar.getInstance();
        dateTimeCal.set(
            Integer.valueOf(whenDate.substring(0, 4)),      // year
            Integer.valueOf(whenDate.substring(4, 6)) - 1,  // month
            Integer.valueOf(whenDate.substring(6, 8)),      // date
            Integer.valueOf(whenTime.substring(0, 2)),      // hoursOfDay
            Integer.valueOf(whenTime.substring(2, 4)),      // minute
            Integer.valueOf(whenTime.substring(4, 6))       // second
        );
        
        final long dateTimeMs = dateTimeCal.getTime().getTime();

        // 기록 이미지 저장
        final StringBuilder fileUrlSb = new StringBuilder(256);
        final MultipartFile[] recordsPic = postRecordsDto.getPics();
        final String mFileDir = env.getProperty("foodiy.records.img.basedir");
        String fileName = selectedRecordsDao.getPicUrls();
        String[] fileNameAry = (fileName != null ? fileName.split("`") : null);
        
        for (int rPicIdx = 0; rPicIdx < recordsPic.length; ++rPicIdx) {
            try {
                final MultipartFile mMpFile = recordsPic[rPicIdx];
                final String mOriName = mMpFile.getOriginalFilename();
                final String fileExt = mOriName.substring(mOriName.lastIndexOf("."), mOriName.length());

                fileName = (fileNameAry == null ? null : fileNameAry[rPicIdx]);

                if (fileName == null) {
                    // 기존 파일명 없으면 새로 생성
                    Calendar todayCal = Calendar.getInstance();
                    final String filePrefix = new SimpleDateFormat("yyyyMMddHHmmss").format(todayCal.getTime()) + "-" + writeUserId;
                    fileName = filePrefix + "-" + Hex.encodeHexString(MdUtil.sha128((mOriName + System.currentTimeMillis()).getBytes()), true) + fileExt;
                }
                else {
                    // 기존 파일명 있으면 확장자만 교체
                    int extIdx = fileName.lastIndexOf(".");

                    if (extIdx == -1) {
                        fileName = fileName + fileExt;
                    }
                    else {
                        fileName = fileName.substring(0, extIdx) + fileExt;
                    }
                }

                StorageUtil.storeMultipartAsFile(recordsPic[rPicIdx], mFileDir, fileName);
                fileUrlSb.append(fileName != null ? fileName : "").append("`");
            }
            catch (NullPointerException | IndexOutOfBoundsException | IllegalArgumentException | IOException e) {
                logger.warn("Exception while storing menus img!", e);
                continue;
            }
        }

        if (fileUrlSb.length() > 0) {
            fileUrlSb.setLength(fileUrlSb.length() - 1);
            fileName = fileUrlSb.toString();
        }

        recordsDao.setId(selectedRecordsDao.getId());
        recordsDao.setWriteUserId(writeUserId);
        recordsDao.setTitle(postRecordsDto.getTitle());
        recordsDao.setWhenDate(new Date(dateTimeMs));
        recordsDao.setWhenTime(new Time(dateTimeMs));
        recordsDao.setWhereLati(postRecordsDto.getWhereLati());
        recordsDao.setWhereLongi(postRecordsDto.getWhereLongi());
        recordsDao.setWherePlace(postRecordsDto.getWherePlace());
        recordsDao.setWhoWith(postRecordsDto.getWhoWith());
        recordsDao.setPicUrls(fileName);

        // DB데이터 수정 (RecordsMapper.xml)
        try {
            recordsMapper.update(recordsDao);
        }
        catch (Exception e) {
            logger.error("RecordsDB update Exception!", e);
            return ApiResult.make(false, ApiResult.DEFAULT_API_RESULT_CODE_NEGATIVE, "기록 수정에 실패했습니다.");
        }

        logger.info("RecordsDB update success! (recordsDao: " + recordsDao.toString() + ")");
        return ApiResult.make(true, MapUtil.toMap("updatedRecords", recordsDao));
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

        // 사용자 인증
        ApiResult userAuthResult = usersService.checkUserStatus(userJwt);

        if (userAuthResult == null || userAuthResult.getResult() == false) {
            logger.error("Fail to auth user! (userJwt: " + userJwt + ")");
            return userAuthResult;
        }

        // 기록 조회
        ApiResult selectResult = selectRecordById(userJwt, id);

        if (selectResult == null || !selectResult.getResult()) {
            return selectResult;
        }

        RecordsDao selectedDao = (RecordsDao) selectResult.getData("selectedRecord");

        if (selectedDao == null) {
            return ApiResult.make(false, ApiResult.DEFAULT_API_RESULT_CODE_NEGATIVE, "원본 기록 찾기 실패! 삭제에 실패했습니다.");
        }

        // 기록 작성자와 사용자 일치 확인
        long writeUserId = Long.valueOf(userAuthResult.getDataAsStr("id"));

        if (selectedDao != null && selectedDao.getWriteUserId() != writeUserId) {
            // 작성자가 아닌 경우 조회실패
            logger.error("write_user_id != writerId (writer_user_id: " + selectedDao.getWriteUserId() +
                         ", writerId: " + writeUserId + ")");
            selectedDao = null;
        }

        // 결과 반환
        if (selectedDao == null) {
            logger.error("저장된 기록 찾기에 실패했습니다.");
            return ApiResult.make(false, ApiResult.DEFAULT_API_RESULT_CODE_NEGATIVE, "저장된 기록 찾기에 실패했습니다.");
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
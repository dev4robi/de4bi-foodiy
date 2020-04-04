package com.robi.foodiy.service;

import java.io.IOException;
import java.util.List;

import com.robi.data.ApiResult;
import com.robi.foodiy.data.dao.MenusDao;
import com.robi.foodiy.data.dto.PostMenusDto;
import com.robi.foodiy.mapper.MenusMapper;
import com.robi.util.MapUtil;
import com.robi.util.StorageUtil;
import com.robi.util.ValidatorUtil;

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
public class MenusService {

    private static Logger logger = LoggerFactory.getLogger(MenusService.class);

    private final UsersService usersService;

    private final MenusMapper menusMapper;

    private final Environment env;

    public ApiResult getMenusByPageIdx(final String userJwt, final int pageIdx) {
        // 파라미터 검사
        ApiResult validResult = null;

        if (!(validResult = ValidatorUtil.nullOrZeroLen("user_jwt", userJwt)).getResult()) {
            logger.error(validResult.getResultMsg());
            return validResult;
        }

        if (!(validResult = ValidatorUtil.arthimatic("page_idx", pageIdx, 0, Integer.MAX_VALUE)).getResult()) {
            logger.error(validResult.getResultMsg());
            return validResult;
        }

        // 사용자 인증
        final ApiResult userAuthResult = usersService.checkUserStatus(userJwt);

        if (userAuthResult == null || userAuthResult.getResult() == false) {
            logger.error("Fail to auth user! (userJwt: " + userJwt + ")");
            return userAuthResult;
        }

        // DB 조회 (MenusMapper.xml)
        final long writerId = Long.valueOf(userAuthResult.getDataAsStr("id"));
        List<MenusDao> selMenusList = null;

        try {
            selMenusList = menusMapper.selectAllByWriterIdToPage(writerId, pageIdx, 10);
        }
        catch (final Exception e) {
            logger.error("Menus DB Exception!", e);
        }

        // 성공응답
        return ApiResult.make(true, MapUtil.toMap("selectedMenusList", selMenusList));
    }

    public ApiResult getMenusByMenuName(final String userJwt, final String menuName, final int pageIdx) {
        // 파라미터 검사
        ApiResult validResult = null;

        if (!(validResult = ValidatorUtil.nullOrZeroLen("user_jwt", userJwt)).getResult()) {
            logger.error(validResult.getResultMsg());
            return validResult;
        }

        if (!(validResult = ValidatorUtil.nullOrZeroLen("menu_name", menuName)).getResult()) {
            logger.error(validResult.getResultMsg());
            return validResult;
        }

        if (!(validResult = ValidatorUtil.arthimatic("pageIdx", pageIdx, 0, Integer.MAX_VALUE)).getResult()) {
            logger.error(validResult.getResultMsg());
            return validResult;
        }

        // 사용자 인증
        final ApiResult userAuthResult = usersService.checkUserStatus(userJwt);

        if (userAuthResult == null || userAuthResult.getResult() == false) {
            logger.error("Fail to auth user! (userJwt: " + userJwt + ")");
            return userAuthResult;
        }

        // DB 조회 (MenusMapper.xml)
        final long writerId = Long.valueOf(userAuthResult.getDataAsStr("id"));
        List<MenusDao> selMenusList = null;

        try {
            selMenusList = menusMapper.selectAllByWriterIdAndMenuNameToPage(writerId, menuName, pageIdx, 10);
        }
        catch (final Exception e) {
            logger.error("Menus DB Exception!", e);
        }

        // 성공응답
        return ApiResult.make(true, MapUtil.toMap("selectedMenusList", selMenusList));
    }

    public ApiResult getMenusByTag(final String userJwt, final String tag, final int pageIdx) {
        // 파라미터 검사
        ApiResult validResult = null;

        if (!(validResult = ValidatorUtil.nullOrZeroLen("user_jwt", userJwt)).getResult()) {
            logger.error(validResult.getResultMsg());
            return validResult;
        }

        if (!(validResult = ValidatorUtil.nullOrZeroLen("tag", tag)).getResult()) {
            logger.error(validResult.getResultMsg());
            return validResult;
        }

        if (!(validResult = ValidatorUtil.arthimatic("pageIdx", pageIdx, 0, Integer.MAX_VALUE)).getResult()) {
            logger.error(validResult.getResultMsg());
            return validResult;
        }

        // 사용자 인증
        final ApiResult userAuthResult = usersService.checkUserStatus(userJwt);

        if (userAuthResult == null || userAuthResult.getResult() == false) {
            logger.error("Fail to auth user! (userJwt: " + userJwt + ")");
            return userAuthResult;
        }

        // DB 조회 (MenusMapper.xml)
        final long writerId = Long.valueOf(userAuthResult.getDataAsStr("id"));
        List<MenusDao> selMenusList = null;

        try {
            selMenusList = menusMapper.selectAllByWriterIdAndTagToPage(writerId, tag, pageIdx, 10);
        }
        catch (final Exception e) {
            logger.error("Menus DB Exception!", e);
        }

        // 성공응답
        return ApiResult.make(true, MapUtil.toMap("selectedMenusList", selMenusList));
    }

    public ApiResult getMenusByPlace(final String userJwt, final String place, final int pageIdx) {
        // 파라미터 검사
        ApiResult validResult = null;

        if (!(validResult = ValidatorUtil.nullOrZeroLen("user_jwt", userJwt)).getResult()) {
            logger.error(validResult.getResultMsg());
            return validResult;
        }

        if (!(validResult = ValidatorUtil.nullOrZeroLen("place", place)).getResult()) {
            logger.error(validResult.getResultMsg());
            return validResult;
        }

        if (!(validResult = ValidatorUtil.arthimatic("pageIdx", pageIdx, 0, Integer.MAX_VALUE)).getResult()) {
            logger.error(validResult.getResultMsg());
            return validResult;
        }

        // 사용자 인증
        final ApiResult userAuthResult = usersService.checkUserStatus(userJwt);

        if (userAuthResult == null || userAuthResult.getResult() == false) {
            logger.error("Fail to auth user! (userJwt: " + userJwt + ")");
            return userAuthResult;
        }

        // DB 조회 (MenusMapper.xml)
        final long writerId = Long.valueOf(userAuthResult.getDataAsStr("id"));
        List<MenusDao> selMenusList = null;

        try {
            selMenusList = menusMapper.selectAllByWriterIdAndPlaceToPage(writerId, place, pageIdx, 10);
        }
        catch (final Exception e) {
            logger.error("Menus DB Exception!", e);
        }

        // 성공응답
        return ApiResult.make(true, MapUtil.toMap("selectedMenusList", selMenusList));
    }

    public ApiResult getMenusByWho(final String userJwt, final String who, final int pageIdx) {
        // 파라미터 검사
        ApiResult validResult = null;

        if (!(validResult = ValidatorUtil.nullOrZeroLen("user_jwt", userJwt)).getResult()) {
            logger.error(validResult.getResultMsg());
            return validResult;
        }

        if (!(validResult = ValidatorUtil.nullOrZeroLen("who", who)).getResult()) {
            logger.error(validResult.getResultMsg());
            return validResult;
        }

        if (!(validResult = ValidatorUtil.arthimatic("pageIdx", pageIdx, 0, Integer.MAX_VALUE)).getResult()) {
            logger.error(validResult.getResultMsg());
            return validResult;
        }

        // 사용자 인증
        final ApiResult userAuthResult = usersService.checkUserStatus(userJwt);

        if (userAuthResult == null || userAuthResult.getResult() == false) {
            logger.error("Fail to auth user! (userJwt: " + userJwt + ")");
            return userAuthResult;
        }

        // DB 조회 (MenusMapper.xml)
        final long writerId = Long.valueOf(userAuthResult.getDataAsStr("id"));
        List<MenusDao> selMenusList = null;

        try {
            selMenusList = menusMapper.selectAllByWriterIdAndWhoToPage(writerId, who, pageIdx, 10);
        }
        catch (final Exception e) {
            logger.error("Menus DB Exception!", e);
        }

        // 성공응답
        return ApiResult.make(true, MapUtil.toMap("selectedMenusList", selMenusList));
    }

    /**
     * <p>userJwt내의 user_id를 write_user_id로 하여 record추가합니다.</p>
     * <p>(※순서: 파라미터 검사 -> 사용자 인증 -> 이미지 저장 -> MenusDao생성 -> Menus DB Update -> 결과 반환)</p>
     * @param userJwt : auth서버로부터 발급된 JWT
     * @param menuId : 업데이트할 메뉴ID
     * @param postMenusDtoAry : 업데이트할 메뉴 데이터 배열
     * @return ApiResult
     */
    public ApiResult putMenus(final String userJwt, final long menuId, final PostMenusDto[] postMenusDtoAry) {
        // 파라미터 검사
        ApiResult validResult = null;

        if (!(validResult = ValidatorUtil.nullOrZeroLen("user_jwt", userJwt)).getResult()) {
            logger.error(validResult.getResultMsg());
            return validResult;
        }

        if (postMenusDtoAry == null || postMenusDtoAry.length == 0 || postMenusDtoAry[0] == null) {
            logger.error(validResult.getResultMsg());
            return ApiResult.make(false, "수정할 메뉴 파라미터가 없습니다.");
        }

        // 사용자 인증
        final ApiResult userAuthResult = usersService.checkUserStatus(userJwt);

        if (userAuthResult == null || userAuthResult.getResult() == false) {
            logger.error("Fail to auth user! (userJwt: " + userJwt + ")");
            return userAuthResult;
        }

        // DB 조회 (MenusMapper.xml) 
        final long writerId = Long.valueOf(userAuthResult.getDataAsStr("id"));
        MenusDao selMenus = null;

        try {
            selMenus = menusMapper.selectById(menuId);
        }
        catch (final Exception e) {
            logger.error("Menus DB Select Exception!", e);
        }

        if (selMenus == null) {
            return ApiResult.make(false, "해당 메뉴를 찾을 수 없습니다.");
        }

        // 작성자와 삭제 요청자가 일치하는지 비교
        if (writerId != selMenus.getWriteUserId()) {
            return ApiResult.make(false, "메뉴 작성자와 삭제 요청자가 일치하지 않습니다.");
        }

        // 메뉴 이미지 저장
        final StringBuilder fileUrlSb = new StringBuilder(256);
        final PostMenusDto menusDto = postMenusDtoAry[0];
        final MultipartFile[] menusPic = menusDto.getMenuPics();
        final String mFileDir = env.getProperty("foodiy.records.img.basedir");
        
        for (int mPicIdx = 0; mPicIdx < menusPic.length; ++mPicIdx) {
            try {
                final MultipartFile mMpFile = menusPic[mPicIdx];
                final String mOriName = mMpFile.getOriginalFilename();
                final String fileExt = mOriName.substring(mOriName.lastIndexOf("."), mOriName.length());
                final String fileName = selMenus.getPicUrl();
                final String fileUrl =  StorageUtil.storeMultipartAsFile(menusPic[mPicIdx], mFileDir, fileName);
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

        // DTO -> DAO 변환
        final MenusDao menusDao = new MenusDao();
        menusDao.setId(menuId);
        menusDao.setWriteUserId(selMenus.getWriteUserId());
        menusDao.setRecordId(selMenus.getRecordId());
        menusDao.setName(menusDto.getMenuName());
        menusDao.setPicUrl(fileUrlSb.toString());
        menusDao.setPrice(Integer.valueOf(menusDto.getMenuPrice()));
        menusDao.setScore(Integer.valueOf(menusDto.getMenuPrice()));
        menusDao.setTags(menusDto.getMenuTag());

        // DB 업데이트 (MenusMapper.xml)
        try {
            menusMapper.update(menusDao);
        }
        catch (final Exception e) {
            logger.error("Menus DB Select Exception!", e);
        }

        // 로깅 및 응답
        logger.info("Menus update success! (menusDao: " + menusDao.toString() + ")");
        return ApiResult.make(true, MapUtil.toMap("updatedMenus", menusDao));

        // 이거 테스트부터 수행 @@
    }

    @Transactional
    public ApiResult deleteMenus(final String userJwt, final long menuId) {
        // 파라미터 검사
        ApiResult validResult = null;

        if (!(validResult = ValidatorUtil.nullOrZeroLen("user_jwt", userJwt)).getResult()) {
            logger.error(validResult.getResultMsg());
            return validResult;
        }

        // 사용자 인증
        final ApiResult userAuthResult = usersService.checkUserStatus(userJwt);

        if (userAuthResult == null || userAuthResult.getResult() == false) {
            logger.error("Fail to auth user! (userJwt: " + userJwt + ")");
            return userAuthResult;
        }

        // DB 조회 (MenusMapper.xml) 
        final long writerId = Long.valueOf(userAuthResult.getDataAsStr("id"));
        MenusDao selMenus = null;

        try {
            selMenus = menusMapper.selectById(menuId);
        }
        catch (final Exception e) {
            logger.error("Menus DB Select Exception!", e);
        }

        if (selMenus == null) {
            return ApiResult.make(false, "해당 메뉴를 찾을 수 없습니다.");
        }

        // 작성자와 삭제 요청자가 일치하는지 비교
        if (writerId != selMenus.getWriteUserId()) {
            return ApiResult.make(false, "메뉴 작성자와 삭제 요청자가 일치하지 않습니다.");
        }

        // DB 삭제 (MenusMapper.xml)
        try {
            menusMapper.deleteById(menuId);
        }
        catch (final Exception e) {
            logger.error("Menus Delete DB Exception!", e);
        }

        logger.info("Menus delete success! (menuId: " + menuId + ")");
        return ApiResult.make(true);
    }
}
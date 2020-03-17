package com.robi.foodiy.service;

import java.util.List;

import com.robi.data.ApiResult;
import com.robi.foodiy.data.dao.MenusDao;
import com.robi.foodiy.mapper.MenusMapper;
import com.robi.util.MapUtil;
import com.robi.util.ValidatorUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class MenusService {

    private static Logger logger = LoggerFactory.getLogger(MenusService.class);

    private UsersService usersService;

    private MenusMapper menusMapper;

    public ApiResult getMenusByPageIdx(String userJwt, int pageIdx) {
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
        ApiResult userAuthResult = usersService.checkUserStatus(userJwt);

        if (userAuthResult == null || userAuthResult.getResult() == false) {
            logger.error("Fail to auth user! (userJwt: " + userJwt + ")");
            return userAuthResult;
        }

        // DB 조회 (MenusMapper.xml)
        long writerId = Long.valueOf(userAuthResult.getDataAsStr("id"));
        List<MenusDao> selMenusList = null;

        try {
            selMenusList = menusMapper.selectAllByWriterIdToPage(writerId, pageIdx, 10);
        }
        catch (Exception e) {
            logger.error("Menus DB Exception!", e);
        }

        // 성공응답
        return ApiResult.make(true, MapUtil.toMap("selectedMenusList", selMenusList));
    }

    public ApiResult getMenusByMenuName(String userJwt, String menuName, int pageIdx) {
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
        ApiResult userAuthResult = usersService.checkUserStatus(userJwt);

        if (userAuthResult == null || userAuthResult.getResult() == false) {
            logger.error("Fail to auth user! (userJwt: " + userJwt + ")");
            return userAuthResult;
        }

        // DB 조회 (MenusMapper.xml)
        long writerId = Long.valueOf(userAuthResult.getDataAsStr("id"));
        List<MenusDao> selMenusList = null;

        try {
            selMenusList = menusMapper.selectAllByWriterIdAndMenuNameToPage(writerId, menuName, pageIdx, 10);
        }
        catch (Exception e) {
            logger.error("Menus DB Exception!", e);
        }

        // 성공응답
        return ApiResult.make(true, MapUtil.toMap("selectedMenusList", selMenusList));
    }

    public ApiResult getMenusByTag(String userJwt, String tag, int pageIdx) {
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
        ApiResult userAuthResult = usersService.checkUserStatus(userJwt);

        if (userAuthResult == null || userAuthResult.getResult() == false) {
            logger.error("Fail to auth user! (userJwt: " + userJwt + ")");
            return userAuthResult;
        }

        // DB 조회 (MenusMapper.xml)
        long writerId = Long.valueOf(userAuthResult.getDataAsStr("id"));
        List<MenusDao> selMenusList = null;

        try {
            selMenusList = menusMapper.selectAllByWriterIdAndTagToPage(writerId, tag, pageIdx, 10);
        }
        catch (Exception e) {
            logger.error("Menus DB Exception!", e);
        }

        // 성공응답
        return ApiResult.make(true, MapUtil.toMap("selectedMenusList", selMenusList));
    }

    public ApiResult getMenusByPlace(String userJwt, String place, int pageIdx) {
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
        ApiResult userAuthResult = usersService.checkUserStatus(userJwt);

        if (userAuthResult == null || userAuthResult.getResult() == false) {
            logger.error("Fail to auth user! (userJwt: " + userJwt + ")");
            return userAuthResult;
        }

        // DB 조회 (MenusMapper.xml)
        long writerId = Long.valueOf(userAuthResult.getDataAsStr("id"));
        List<MenusDao> selMenusList = null;

        try {
            selMenusList = menusMapper.selectAllByWriterIdAndPlaceToPage(writerId, place, pageIdx, 10);
        }
        catch (Exception e) {
            logger.error("Menus DB Exception!", e);
        }

        // 성공응답
        return ApiResult.make(true, MapUtil.toMap("selectedMenusList", selMenusList));
    }

    public ApiResult getMenusByWho(String userJwt, String who, int pageIdx) {
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
        ApiResult userAuthResult = usersService.checkUserStatus(userJwt);

        if (userAuthResult == null || userAuthResult.getResult() == false) {
            logger.error("Fail to auth user! (userJwt: " + userJwt + ")");
            return userAuthResult;
        }

        // DB 조회 (MenusMapper.xml)
        long writerId = Long.valueOf(userAuthResult.getDataAsStr("id"));
        List<MenusDao> selMenusList = null;

        try {
            selMenusList = menusMapper.selectAllByWriterIdAndWhoToPage(writerId, who, pageIdx, 10);
        }
        catch (Exception e) {
            logger.error("Menus DB Exception!", e);
        }

        // 성공응답
        return ApiResult.make(true, MapUtil.toMap("selectedMenusList", selMenusList));
    }
}
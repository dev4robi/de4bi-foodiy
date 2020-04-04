package com.robi.foodiy.controller.api;

import java.util.Arrays;

import com.robi.data.ApiResult;
import com.robi.exception.ApiException;
import com.robi.foodiy.data.dto.PostMenusDto;
import com.robi.foodiy.data.dto.PostRecordsDto;
import com.robi.foodiy.service.MenusService;

import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class MenusController {

    private static Logger logger = LoggerFactory.getLogger(MenusController.class);

    private MenusService menusSvc;

    @GetMapping("/menus/page/{page_idx}")
    public ApiResult getMenusByPage(
        @RequestHeader ("user_jwt") String userJwt,
        @PathVariable ("page_idx")   int pageIdx)
    {
        return menusSvc.getMenusByPageIdx(userJwt, pageIdx);
    }
    
    @GetMapping("/menus/name/{menuName}/page/{page_idx}")
    public ApiResult getMenusByName(
        @RequestHeader ("user_jwt") String userJwt,
        @PathVariable ("menuName")  String menuName,
        @PathVariable ("page_idx")  int pageIdx)
    {
        return menusSvc.getMenusByMenuName(userJwt, menuName, pageIdx);
    }

    @GetMapping("/menus/tag/{tag}/page/{page_idx}")
    public ApiResult getMenusByTag(
        @RequestHeader ("user_jwt") String userJwt,
        @PathVariable ("tag")       String tag,
        @PathVariable ("page_idx")  int pageIdx)
    {
        return menusSvc.getMenusByTag(userJwt, tag, pageIdx);
    }

    @GetMapping("/menus/place/{place}/page/{page_idx}")
    public ApiResult getMenusByPlace(
        @RequestHeader ("user_jwt") String userJwt,
        @PathVariable ("place")     String place,
        @PathVariable ("page_idx")  int pageIdx)
    {
        return menusSvc.getMenusByPlace(userJwt, place, pageIdx);
    }

    @GetMapping("/menus/who/{who}/page/{page_idx}")
    public ApiResult getMenusByWho(
        @RequestHeader ("user_jwt") String userJwt,
        @PathVariable ("who")       String who,
        @PathVariable ("page_idx")  int pageIdx)
    {
        return menusSvc.getMenusByWho(userJwt, who, pageIdx);
    }

    @PutMapping("menus/{menuId}")
    public ApiResult putMenus(
        // header
        @RequestHeader("user_jwt")  String userJwt,
        // body
        @Nullable @PathVariable ("menuId")    long menuId,
        @Nullable @RequestPart("menus")       String menusJsonStr,
        @Nullable @RequestPart("menu_pics")   MultipartFile[] menuPics
    ) {
        // Menus
        PostMenusDto[] postMenusDtoAry = null;
        
        try {
            if (menusJsonStr != null) {
                JSONArray menuAry = new JSONArray(menusJsonStr);
                int menuCnt = menuAry.length();
                postMenusDtoAry = new PostMenusDto[menuCnt];
                int menuPicBaseIdx = 0;                
                
                for (int i = 0; i < menuCnt; ++i) {
                    postMenusDtoAry[i] = new PostMenusDto();
                    JSONArray menu = menuAry.getJSONArray(i);
                    postMenusDtoAry[i].setMenuName(menu.get(0).toString());
                    postMenusDtoAry[i].setMenuPrice(menu.get(1).toString());
                    postMenusDtoAry[i].setMenuTag(menu.get(2).toString());
                    postMenusDtoAry[i].setMenuScore(menu.get(3).toString());
                    
                    int menuPicCnt = Integer.valueOf(menu.get(4).toString());
                    int newMenuPicIdx = menuPicBaseIdx + menuPicCnt;
                    postMenusDtoAry[i].setMenuPics(Arrays.copyOfRange(menuPics, menuPicBaseIdx, newMenuPicIdx));
                    menuPicBaseIdx = newMenuPicIdx;
                }
            }
        }
        catch (Exception e) {
            logger.error("Exception! {}", e);
            throw new ApiException("레코드/메뉴 파라미터 오류!");
        }
        
        return menusSvc.putMenus(userJwt, menuId, postMenusDtoAry);
    }

    @DeleteMapping("menus/{menu_id}")
    public ApiResult deleteMenus(
        @RequestHeader ("user_jwt") String userJwt,
        @PathVariable ("menu_id") long menuId
    ) {
        return menusSvc.deleteMenus(userJwt, menuId);
    }
}
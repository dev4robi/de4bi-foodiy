package com.robi.foodiy.controller.api;

import java.util.List;

import com.robi.data.ApiResult;
import com.robi.foodiy.data.dto.PostRecordsDto;
import com.robi.foodiy.service.RecordsWithMenusService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class RecordsController {

    private static final Logger logger = LoggerFactory.getLogger(RecordsController.class);

    RecordsWithMenusService recordsWithMenusSvc;

    @GetMapping("/records/{id}")
    public ApiResult getRecords(
        @RequestHeader("user_jwt") String userJwt,
        @PathVariable("id") long id
    ) {
        return recordsWithMenusSvc.selectRecordById(userJwt, id);
    }

    @PostMapping("/records")
    public ApiResult postRecordsWithMenus(
        @RequestHeader("user_jwt")  String userJwt,
        // records
        @Nullable @RequestPart("title")       String title,
        @Nullable @RequestPart("when_date")   String whenDate,
        @Nullable @RequestPart("when_time")   String whenTime,
        @Nullable @RequestPart("where_lati")  String whereLati,
        @Nullable @RequestPart("where_longi") String whereLongi,
        @Nullable @RequestPart("who_with")    String whoWith,
        @Nullable @RequestPart("pics")        MultipartFile[] pics,
        // menus
        @Nullable @RequestPart("menu_names")  String menuNames,
        @Nullable @RequestPart("menu_pirces") String menuPrices,
        @Nullable @RequestPart("menu_tags")   String menuTags,
        @Nullable @RequestPart("menu_scores") String menuScores,
        @Nullable @RequestPart("menu_pics")   MultipartFile[] menuPics
    ) {
        // [Note]
        // Content-Type: multipart/form-data 특성상 @RequestBody, @RequestParam,
        // @ModelAttribute 등의 어노테이션 기반의 DTO자동 생성이 원활하지 않았다.
        // 몇가지 연구결과, Multipart를 전송하는 경우에는 컨트롤러에서 수동으로
        // DTO객체를 생성하도록 만드는 것이 가장 합리적인 선택으로 판단되었다.
        // 또한, @RequestPart 어노테이션은 MultipartFile[]에 대해서는 스트림으로
        // 처리가 가능하나, String[] 등 다른 클래스에 대해서는 처리하지 못하기에,
        // menuNames등 menus항목에 대해서는 "`" 문자를 구분자로 전송하여
        // 컨트롤러단에서 split 하도록 로직이 작성되었다.

        PostRecordsDto postRecordsDto = new PostRecordsDto();

        return recordsWithMenusSvc.insertRecordsWithMenus(userJwt, postRecordsDto);
    }
}
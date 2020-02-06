package com.robi.foodiy.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class PostRecordsDto {
    // 레코드 데이터
    @JsonProperty("title")
    String title;
    
    @JsonProperty("when_date")
    String whenDate;

    @JsonProperty("when_time")
    String whenTime;
    
    @JsonProperty("where_lati")
    String whereLati;

    @JsonProperty("where_longi")
    String whereLongi;

    @JsonProperty("who_with")
    String whoWith;

    @JsonProperty("pic_1")
    MultipartFile pic1;

    @JsonProperty("pic_2")
    MultipartFile pic2;

    @JsonProperty("pic_3")
    MultipartFile pic3;

    // [Note]
    // 이하 데이터들은 원래 별도의 MenusDTO 클래스로 구성했지만,
    // 이미지 전송을 위해 ajax Content-Type을 form/multipart을 사용할 수 밖에 없었고,
    // 해당 Content-Type의 전송방식 특성상 배열등에 담아서 보낼 수 없었다.
    // 사진 데이터들만 따로 전송하는 방법이 있겠지만, 이번 프로젝트에서는
    // 수동으로 길이를 다루는 방식으로 접근해 보자.

    // 메뉴데이터
    // 1
    @JsonProperty("menu_name_1")    String menuName1;
    @JsonProperty("menu_price_1")   String menuPrice1;
    @JsonProperty("menu_tags_1")    String menuTags1;
    @JsonProperty("menu_score_1")   String menuSocre1;
    @JsonProperty("menu_pics_1")    MultipartFile menuPics1;
    // 2
    @JsonProperty("menu_name_2")    String menuName2;
    @JsonProperty("menu_price_2")   String menuPrice2;
    @JsonProperty("menu_tags_2")    String menuTags2;
    @JsonProperty("menu_score_2")   String menuSocre2;
    @JsonProperty("menu_pics_2")    MultipartFile menuPics2;
    // 3
    @JsonProperty("menu_name_3")    String menuName3;
    @JsonProperty("menu_price_3")   String menuPrice3;
    @JsonProperty("menu_tags_3")    String menuTags3;
    @JsonProperty("menu_score_3")   String menuSocre3;
    @JsonProperty("menu_pics_3")    MultipartFile menuPics3;
    // 4
    @JsonProperty("menu_name_4")    String menuName4;
    @JsonProperty("menu_price_4")   String menuPrice4;
    @JsonProperty("menu_tags_4")    String menuTags4;
    @JsonProperty("menu_score_4")   String menuSocre4;
    @JsonProperty("menu_pics_4")    MultipartFile menuPics4;
    // 5
    @JsonProperty("menu_name_5")    String menuName5;
    @JsonProperty("menu_price_5")   String menuPrice5;
    @JsonProperty("menu_tags_5")    String menuTags5;
    @JsonProperty("menu_score_5")   String menuSocre5;
    @JsonProperty("menu_pics_5")    MultipartFile menuPics5;
    // 6
    @JsonProperty("menu_name_6")    String menuName6;
    @JsonProperty("menu_price_6")   String menuPrice6;
    @JsonProperty("menu_tags_6")    String menuTags6;
    @JsonProperty("menu_score_6")   String menuSocre6;
    @JsonProperty("menu_pics_6")    MultipartFile menuPics6;
}
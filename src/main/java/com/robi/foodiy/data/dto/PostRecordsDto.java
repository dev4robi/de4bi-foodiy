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
    float whereLati;

    @JsonProperty("where_longi")
    float whereLongi;

    @JsonProperty("where_place")
    String wherePlace;

    @JsonProperty("who_with")
    String whoWith;

    @JsonProperty("pics")
    MultipartFile[] pics;

    // 메뉴데이터
    @JsonProperty("menu_names")
    String[] menuNames;

    @JsonProperty("menu_prices")
    String[] menuPrices;

    @JsonProperty("menu_tags")
    String[] menuTags;

    @JsonProperty("menu_scores")
    String[] menuScores;

    @JsonProperty("menu_pics")
    MultipartFile[] menuPics;
}
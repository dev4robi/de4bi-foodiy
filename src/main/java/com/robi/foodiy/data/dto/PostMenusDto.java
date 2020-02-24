package com.robi.foodiy.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class PostMenusDto {
    @JsonProperty("menu_name")
    String menuName;

    @JsonProperty("menu_price")
    String menuPrice;

    @JsonProperty("menu_tag")
    String menuTag;

    @JsonProperty("menu_score")
    String menuScore;

    @JsonProperty("menu_pics")
    MultipartFile[] menuPics;
}
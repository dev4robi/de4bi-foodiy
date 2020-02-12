package com.robi.foodiy.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class TestDto {

    @JsonProperty("name")
    String name;

    //@JsonProperty("pic")
    //MultipartFile pic;

    @JsonProperty("sub_name")
    String subName;

    //@JsonProperty("sub_pics")
    //MultipartFile subPics;
}
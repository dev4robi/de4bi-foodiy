package com.robi.foodiy.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class TestSubDto {

    @JsonProperty("sub_name")
    String subName;

    @JsonProperty("sub_pics")
    MultipartFile[] subPics;
}
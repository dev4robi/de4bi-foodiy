package com.robi.foodiy.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class PostRecordsDto {
    // 레코드 데이터
    @JsonProperty("id")
    long id;

    @JsonProperty("title")
    String title;
    
    @JsonProperty("when_date")
    String whenDate;

    @JsonProperty("when_time")
    String whenTime;
    
    @JsonProperty("where_lati")
    Double whereLati;

    @JsonProperty("where_longi")
    Double whereLongi;

    @JsonProperty("where_place")
    String wherePlace;

    @JsonProperty("who_with")
    String whoWith;

    @JsonProperty("pics")
    MultipartFile[] pics;
}
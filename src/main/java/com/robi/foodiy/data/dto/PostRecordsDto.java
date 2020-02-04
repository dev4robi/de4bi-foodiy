package com.robi.foodiy.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PostRecordsDto {
    
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

    @JsonProperty("menus")
    PostMenusDto[] menus;
}